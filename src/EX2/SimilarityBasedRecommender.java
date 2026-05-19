package EX2;

import java.util.*;

import static java.util.stream.Collectors.*;

/** Similarity-based recommender with bias correction. */
class SimilarityBasedRecommender<T extends Item> extends RecommenderSystem<T>
{
    private final double globalBias;
    private final Map<Integer, Double> itemBiases;
    private final Map<Integer, Double> userBiases;
    private final Map<Integer, Map<Integer, Double>> biasFreeRatingsByUser;

    // בנאי: מחשב מראש את כל ההטיות ואת הדירוגים המתוקנים
    public SimilarityBasedRecommender(Map<Integer, User> users, Map<Integer, T> items, List<Rating<T>> ratings)
    {
        super(users, items, ratings);

        // הממוצע הכללי של כל הדירוגים במערכת
        this.globalBias = ratings.stream()
                .mapToDouble(r -> r.getRating())
                .average()
                .orElse(0.0);

        // לכל פריט: כמה הדירוגים שלו גבוהים/נמוכים מהממוצע הכללי
        this.itemBiases = ratings.stream()
                .collect(groupingBy(
                        r -> r.getItemId(),
                        averagingDouble(r -> r.getRating() - globalBias)
                ));

        // לכל משתמש: האם הוא נוטה לדרג גבוה/נמוך יחסית, אחרי תיקון לפי הפריט
        this.userBiases = ratings.stream()
                .collect(groupingBy(
                        r -> r.getUserId(),
                        averagingDouble(r -> r.getRating()
                                - globalBias
                                - itemBiases.getOrDefault(r.getItemId(), 0.0))
                ));

        // לכל משתמש נשמרים רק הפריטים שהוא דירג, עם דירוג אחרי הורדת כל ההטיות
        this.biasFreeRatingsByUser = ratings.stream()
                .collect(groupingBy(
                        r -> r.getUserId(),
                        toMap(
                                r -> r.getItemId(),
                                r -> r.getRating()
                                        - globalBias
                                        - itemBiases.getOrDefault(r.getItemId(), 0.0)
                                        - userBiases.getOrDefault(r.getUserId(), 0.0),
                                (oldValue, newValue) -> oldValue
                        )
                ));
    }

    /** Dot-product similarity; 0 if <10 shared items. */
    public double getSimilarity(int u1, int u2)
    {
        // הדירוגים המתוקנים של המשתמש הראשון
        Map<Integer, Double> u1Ratings =
                biasFreeRatingsByUser.getOrDefault(u1, Collections.emptyMap());

        // הדירוגים המתוקנים של המשתמש השני
        Map<Integer, Double> u2Ratings =
                biasFreeRatingsByUser.getOrDefault(u2, Collections.emptyMap());

        // הפריטים ששני המשתמשים דירגו
        Set<Integer> sharedItems = u1Ratings.keySet().stream()
                .filter(itemId -> u2Ratings.containsKey(itemId))
                .collect(toSet());

        // אם אין מספיק פריטים משותפים, הדמיון לא מספיק אמין
        if (sharedItems.size() < 10) {
            return 0.0;
        }

        // חישוב דמיון: סכום מכפלות של הדירוגים המתוקנים על הפריטים המשותפים
        return sharedItems.stream()
                .mapToDouble(itemId -> u1Ratings.get(itemId) * u2Ratings.get(itemId))
                .sum();
    }

    @Override
    public List<T> recommendTop10(int userId)
    {
        // הפריטים שהמשתמש כבר דירג, כדי שלא נמליץ עליהם שוב
        Set<Integer> rated = getRatedItems(userId);

        // המשתמשים הכי דומים למשתמש הנוכחי
        List<Integer> similar = getTopSimilarUsers(userId);

        return ratings.stream()
                // משאירים רק דירוגים של משתמשים דומים
                .filter(r -> similar.contains(r.getUserId()))

                // לא ממליצים על פריטים שהמשתמש כבר דירג
                .filter(r -> !rated.contains(r.getItemId()))

                // מקבצים את הדירוגים לפי פריט
                .collect(groupingBy(r -> r.getItemId()))
                .entrySet().stream()

                // משאירים רק פריטים שיש להם לפחות 5 דירוגים ממשתמשים דומים
                .filter(e -> e.getValue().size() >= 5)

                // ממיינים לפי הציון החזוי, אחר כך כמות דירוגים, ואז שם הפריט
                .sorted(recommendationComparator(userId, similar))

                // מחזירים עד 10 המלצות
                .limit(NUM_OF_RECOMMENDATIONS)

                // הופכים מ-itemId לאובייקט הפריט עצמו
                .map(e -> items.get(e.getKey()))
                .collect(toList());
    }

    private Set<Integer> getRatedItems(int userId)
    {
        // מחזיר את כל מספרי הפריטים שהמשתמש כבר דירג
        return ratings.stream()
                .filter(r -> r.getUserId() == userId)
                .map(r -> r.getItemId())
                .collect(toSet());
    }

    private List<Integer> getTopSimilarUsers(int userId)
    {
        // מוצא את 10 המשתמשים הכי דומים למשתמש הנוכחי
        return users.keySet().stream()
                .filter(other -> other != userId)
                .filter(other -> getSimilarity(userId, other) != 0.0)
                .sorted(Comparator.comparingDouble(
                        (Integer other) -> getSimilarity(userId, other)
                ).reversed())
                .limit(10)
                .collect(toList());
    }

    private Comparator<Map.Entry<Integer, List<Rating<T>>>> recommendationComparator(
            int userId,
            List<Integer> similarUsers)
    {
        // מגדיר לפי מה ממיינים את ההמלצות
        return Comparator
                .<Map.Entry<Integer, List<Rating<T>>>>comparingDouble(
                        e -> predictRating(userId, e.getKey(), similarUsers)
                ).reversed()
                .thenComparing(
                        Comparator.<Map.Entry<Integer, List<Rating<T>>>>comparingInt(
                                e -> getItemRatingsCount(e.getKey())
                        ).reversed()
                )
                .thenComparing(e -> items.get(e.getKey()).getName());
    }

    private double predictRating(int userId, int itemId, List<Integer> similarUsers)
    {
        // סכום משוקלל: משתמשים דומים יותר משפיעים יותר
        double weightedSum = similarUsers.stream()
                .filter(other -> didUserRateItem(other, itemId))
                .mapToDouble(other ->
                        getSimilarity(userId, other) * getBiasFreeRating(other, itemId))
                .sum();

        // סכום הדמיון של המשתמשים הדומים שדירגו את הפריט
        double similaritySum = similarUsers.stream()
                .filter(other -> didUserRateItem(other, itemId))
                .mapToDouble(other -> getSimilarity(userId, other))
                .sum();

        // תחזית בסיסית לפי ממוצע כללי + הטיית פריט + הטיית משתמש
        double base = globalBias
                + getItemBiasValue(itemId)
                + getUserBiasValue(userId);

        // אם אין מידע ממשתמשים דומים, מחזירים רק את התחזית הבסיסית
        if (similaritySum == 0.0) {
            return base;
        }

        // התחזית הסופית: בסיס + תיקון לפי דירוגי המשתמשים הדומים
        return base + weightedSum / similaritySum;
    }

    private boolean didUserRateItem(int userId, int itemId)
    {
        // בודק האם משתמש מסוים דירג פריט מסוים
        return biasFreeRatingsByUser
                .getOrDefault(userId, Collections.emptyMap())
                .containsKey(itemId);
    }

    private double getBiasFreeRating(int userId, int itemId)
    {
        // מחזיר דירוג מתוקן של משתמש לפריט, ואם אין דירוג מחזיר 0
        return biasFreeRatingsByUser
                .getOrDefault(userId, Collections.emptyMap())
                .getOrDefault(itemId, 0.0);
    }

    private int getItemRatingsCount(int itemId)
    {
        // מחזיר כמה דירוגים יש לפריט במערכת
        return (int) ratings.stream()
                .filter(r -> r.getItemId() == itemId)
                .count();
    }

    private double getItemBiasValue(int itemId)
    {
        // מחזיר את ההטיה של הפריט, ואם אין מידע מחזיר 0
        return itemBiases.getOrDefault(itemId, 0.0);
    }

    private double getUserBiasValue(int userId)
    {
        // מחזיר את ההטיה של המשתמש, ואם אין מידע מחזיר 0
        return userBiases.getOrDefault(userId, 0.0);
    }

    public double getGlobalBias()
    {
        return globalBias;
    }

    public double getItemBias(int itemId)
    {
        return getItemBiasValue(itemId);
    }

    public double getUserBias(int userId)
    {
        return getUserBiasValue(userId);
    }
}
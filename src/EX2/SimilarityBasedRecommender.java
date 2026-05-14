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

    // TODO: add data structures to hold the global/item/user biases
    public SimilarityBasedRecommender(Map<Integer, User> users, Map<Integer, T> items, List<Rating<T>> ratings)
    {
        super(users, items, ratings);

        this.globalBias = ratings.stream()
                .mapToDouble(r -> r.getRating())
                .average()
                .orElse(0.0);

        this.itemBiases = ratings.stream()
                .collect(groupingBy(
                        r -> r.getItemId(),
                        averagingDouble(r -> r.getRating() - globalBias)
                ));

        this.userBiases = ratings.stream()
                .collect(groupingBy(
                        r -> r.getUserId(),
                        averagingDouble(r -> r.getRating()
                                - globalBias
                                - itemBiases.getOrDefault(r.getItemId(), 0.0))
                ));

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

        // TODO: initialize the data structures that hold the global/item/user biases
    }

    /** Dot-product similarity; 0 if <10 shared items. */
    public double getSimilarity(int u1, int u2)
    {
        // TODO: implement

        Map<Integer, Double> u1Ratings =
                biasFreeRatingsByUser.getOrDefault(u1, Collections.emptyMap());

        Map<Integer, Double> u2Ratings =
                biasFreeRatingsByUser.getOrDefault(u2, Collections.emptyMap());

        Set<Integer> sharedItems = u1Ratings.keySet().stream()
                .filter(itemId -> u2Ratings.containsKey(itemId))
                .collect(toSet());

        if (sharedItems.size() < 10) {
            return 0.0;
        }

        return sharedItems.stream()
                .mapToDouble(itemId -> u1Ratings.get(itemId) * u2Ratings.get(itemId))
                .sum();
    }

    @Override
    public List<T> recommendTop10(int userId)
    {
        // TODO: implement

        Set<Integer> rated = getRatedItems(userId);
        List<Integer> similar = getTopSimilarUsers(userId);

        return ratings.stream()
                .filter(r -> similar.contains(r.getUserId()))
                .filter(r -> !rated.contains(r.getItemId()))
                .collect(groupingBy(r -> r.getItemId()))
                .entrySet().stream()
                .filter(e -> e.getValue().size() >= 5)
                .sorted(recommendationComparator(userId, similar))
                .limit(NUM_OF_RECOMMENDATIONS)
                .map(e -> items.get(e.getKey()))
                .collect(toList());
    }

    private Set<Integer> getRatedItems(int userId)
    {
        return ratings.stream()
                .filter(r -> r.getUserId() == userId)
                .map(r -> r.getItemId())
                .collect(toSet());
    }

    private List<Integer> getTopSimilarUsers(int userId)
    {
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
        double weightedSum = similarUsers.stream()
                .filter(other -> didUserRateItem(other, itemId))
                .mapToDouble(other ->
                        getSimilarity(userId, other) * getBiasFreeRating(other, itemId))
                .sum();

        double similaritySum = similarUsers.stream()
                .filter(other -> didUserRateItem(other, itemId))
                .mapToDouble(other -> getSimilarity(userId, other))
                .sum();

        double base = globalBias
                + getItemBiasValue(itemId)
                + getUserBiasValue(userId);

        if (similaritySum == 0.0) {
            return base;
        }

        return base + weightedSum / similaritySum;
    }

    private boolean didUserRateItem(int userId, int itemId)
    {
        return biasFreeRatingsByUser
                .getOrDefault(userId, Collections.emptyMap())
                .containsKey(itemId);
    }

    private double getBiasFreeRating(int userId, int itemId)
    {
        return biasFreeRatingsByUser
                .getOrDefault(userId, Collections.emptyMap())
                .getOrDefault(itemId, 0.0);
    }

    private int getItemRatingsCount(int itemId)
    {
        return (int) ratings.stream()
                .filter(r -> r.getItemId() == itemId)
                .count();
    }

    private double getItemBiasValue(int itemId)
    {
        return itemBiases.getOrDefault(itemId, 0.0);
    }

    private double getUserBiasValue(int userId)
    {
        return userBiases.getOrDefault(userId, 0.0);
    }

    public void printGlobalBias()
    {
        // TODO: fix
        System.out.println("Global bias: " + String.format("%.2f", globalBias));
    }

    public void printItemBias(int itemId)
    {
        // TODO: fix
        System.out.println("Item bias for item " + itemId + ": "
                + String.format("%.2f", getItemBiasValue(itemId)));
    }

    public void printUserBias(int userId)
    {
        // TODO: fix
        System.out.println("User bias for user " + userId + ": "
                + String.format("%.2f", getUserBiasValue(userId)));
    }
}
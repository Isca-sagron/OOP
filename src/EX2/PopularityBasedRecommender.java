package EX2;
import java.util.*;

import static java.util.stream.Collectors.*;

/** Popularity‑based recommender implementation. */
class PopularityBasedRecommender<T extends Item> extends RecommenderSystem<T>
{
    private static final int POPULARITY_THRESHOLD = 100;
    public PopularityBasedRecommender(Map<Integer, User> users, Map<Integer, T> items, List<Rating<T>> ratings)
    {
        super(users, items, ratings);
    }

    @Override
    public List<T> recommendTop10(int userId)
    {
        Set<Integer> ratedItemsByUser = ratings.stream()
                .filter(r -> r.getUserId() == userId)
                .map(r -> r.getItemId())
                .collect(toSet());

        return items.entrySet().stream()
                .filter( n -> !ratedItemsByUser.contains(n.getKey()))
                .filter(n -> getItemRatingsCount(n.getKey()) >= POPULARITY_THRESHOLD )

                .sorted
                        (
                                Comparator.<Map.Entry<Integer, T>>comparingDouble(n-> getItemAverageRating(n.getKey()))
                                        .reversed()
                                        .thenComparing (Comparator.<Map.Entry<Integer, T>>comparingInt(entry -> getItemRatingsCount(entry.getKey()))
                                                .reversed())
                                        .thenComparing(entry -> entry.getValue().getName())
                        )
                .limit(NUM_OF_RECOMMENDATIONS)
                .map(n -> n.getValue())
                .collect(toList());
    }

    public double getItemAverageRating(int itemId)
    {
        return ratings.stream()
                .filter(r-> r.getItemId() == itemId)
                .mapToDouble(r->r.getRating())
                .average()
                .orElse(0.0);
        // TODO: implement

    }
    public int getItemRatingsCount(int itemId)
    {
        return (int) ratings.stream()
                .filter(r-> r.getItemId() == itemId)
                .count();
    }

}
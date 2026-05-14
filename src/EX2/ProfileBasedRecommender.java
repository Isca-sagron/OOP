package EX2;

import java.util.*;

import static java.util.stream.Collectors.*;

/** Profile‑based recommender implementation. */
class ProfileBasedRecommender<T extends Item> extends RecommenderSystem<T> {
    public ProfileBasedRecommender(Map<Integer, User> users,
                                   Map<Integer, T> items,
                                   List<Rating<T>> ratings) {
        super(users, items, ratings);
    }

    @Override
    public List<T> recommendTop10(int userId)
    {
        Set<Integer> matchingUserIds = getMatchingProfileUsers(userId).stream()
                .map(User::getId)
                .collect(toSet());

        Set<Integer> ratedItemsByUser = ratings.stream()
                .filter(r -> r.getUserId() == userId)
                .map(r -> r.getItemId())
                .collect(toSet());

        return ratings.stream()
                .filter(r-> matchingUserIds.contains(r.getUserId()))
                .filter(r-> !(ratedItemsByUser.contains(r.getItemId())))
                .collect(groupingBy(r -> r.getItemId()))
                .entrySet().stream()
                .filter(entry -> entry.getValue().size() >= 5)
                .sorted(
                        Comparator.<Map.Entry<Integer, List<Rating<T>>>>comparingDouble(
                                        entry -> entry.getValue().stream()
                                                .mapToDouble(r -> r.getRating())
                                                .average()
                                                .orElse(0.0)
                                ).reversed()
                                .thenComparing(
                                        Comparator.<Map.Entry<Integer, List<Rating<T>>>>comparingInt(
                                                entry -> entry.getValue().size()
                                        ).reversed()
                                )
                                .thenComparing(entry -> items.get(entry.getKey()).getName())
                )
                .limit(NUM_OF_RECOMMENDATIONS)
                .map(entry -> items.get(entry.getKey()))
                .collect(toList());


        // TODO: implement
    }

    public List<User> getMatchingProfileUsers(int userId)
    {
        User ourUser = users.get(userId);

        return users.entrySet().stream()
                .filter(n -> n.getKey() != userId)
                .filter(n -> n.getValue().getGender().equals(ourUser.getGender()) &&
                        (!(n.getValue().getAge() > ourUser.getAge()+5) && !(n.getValue().getAge() < ourUser.getAge()-5)))
                .map(n-> n.getValue())
                .collect(toList());
        // TODO: implement

    }
}
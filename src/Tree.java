public abstract class Tree implements Comparable, Seasonable {
    protected int height;
    protected Season season;
    protected Color leavesColor;
    // TODO: Add auxiliary fields and functions.

    protected String state;

    Tree(int height, Season season, Color leavesColor)
    {
        this.height = height;
        this.season = season;
        this.leavesColor = leavesColor;
    }

    public Season nextSomeSeason()
    {
        Season currentSeason = getCurrentSeason();
        Season[] seasons = Season.values();
        int nextIndex = (currentSeason.ordinal() + 6) % seasons.length;
        return seasons[nextIndex];
    }
    @Override
    public Season getCurrentSeason() {
        return season;
    }

    @Override
    public int compareTo(Object o) {
        // TODO: Implement.
        return 0;
    }
}

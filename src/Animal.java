public abstract class Animal implements Seasonable,Comparable
{
    protected int weight;
    private Season season;
    protected Color color;
    // TODO: Add auxiliary fields and functions.

    protected String state = "";

    Animal(int weight, Season season, Color color)
    {
        this.weight =weight;
        this.season =season;
        this.color =color;
    }

    @Override
    public Season getCurrentSeason() {
        return season;
    }


    public Season nextSomeSeason()
    {
        Season currentSeason = getCurrentSeason();
        Season[] seasons = Season.values();
        int nextIndex = (currentSeason.ordinal() + 6) % seasons.length;
        return seasons[nextIndex];
    }
    @Override
    public int compareTo(Object o)
    {
        // TODO: Implement.
        return 0;
    }
}

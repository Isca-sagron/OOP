package Ex1;

public abstract class Tree implements Comparable, Seasonable
{
    protected int height;
    protected Season season;
    protected Color leavesColor;

    // TODO: Add auxiliary fields and functions.

    protected String state="";

    Tree(int height, Season season, Color leavesColor)
    {
        this.height = height;
        this.season = season;
        this.leavesColor = leavesColor;
    }

    public void setSeason(Season season)
    {
        this.season = season;
    }

    public Season nextSomeSeason()
    {
        Season currentSeason = getCurrentSeason();
        Season[] seasons = Season.values();
        int nextIndex = (currentSeason.ordinal() + 1) % 4;
        return seasons[nextIndex];
    }
    @Override
    public Season getCurrentSeason() {
        return season;
    }
    @Override
    public void changeSeason()
    {
        season = nextSomeSeason();
    }
    @Override
    public abstract String toString();

    @Override
    public int compareTo(Object o)
    {
        // TODO: Implement.
        if (!(o instanceof Tree))
        {
            throw new ClassCastException("Cannot compare EX1.Tree to non-EX1.Tree");
        }
        Tree otherTree = (Tree) o;
        return Integer.compare(this.height, otherTree.height);    }
}

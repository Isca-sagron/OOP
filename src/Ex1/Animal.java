package Ex1;

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


    public void setSeason(Season season)
    {
        this.season = season;
    }


    @Override
    public abstract String toString();

    public Season nextSomeSeason()
    {
        Season currentSeason = getCurrentSeason();
        Season[] seasons = Season.values();
        int nextIndex = (currentSeason.ordinal() + 1) % 4;
        return seasons[nextIndex];
    }
    @Override
    public int compareTo(Object o)
    {
        if (!(o instanceof Animal))
        {
            throw new ClassCastException("Cannot compare EX1.Animal to non-EX1.Animal");
        }
        Animal otherAnimal = (Animal) o;
        return Integer.compare(this.weight, otherAnimal.weight);
        // TODO: Implement.
    }
    @Override
    public void changeSeason()
    {
        season = nextSomeSeason();
    }
}

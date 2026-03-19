package Ex1;
public class Bear extends Animal
{

    Bear(int weight, Season season)
    {
        // TODO: Implement.
        super(weight,season, Color.BROWN);
    }
    @Override
    public String toString()
    {
        // TODO: Implement.
        return "Bear. "+state+ "My weight is: " +weight+ " and my color is: " +color;
    }
    @Override
    public void changeSeason()
    {
        setSeason(nextSomeSeason());
        Season season = getCurrentSeason();
        state = "";
        color = Color.BROWN;

        switch (season)
        {
            case WINTER:
                weight = (int) Math.round(weight * 0.8);
                state = "I am sleeping. ";
                break;

            case SPRING:
                weight = (int) Math.round(weight * 0.75);
                break;

            case SUMMER:
                weight = (int) Math.round(weight * (4.0 / 3.0));
                break;

            case FALL:
                weight = (int) Math.round(weight * 1.25);
                break;
        }
    }
    // TODO: Implement.

}

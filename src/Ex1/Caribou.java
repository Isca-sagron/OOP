package Ex1;

public class Caribou extends Animal
{

    Caribou(int weight, Season season)
    {
        // TODO: Implement.
        super(weight, season,Color.BROWN);
    }

    @Override
    public String toString()
    {
        // TODO: Implement.
        return "EX1.Caribou: " +state+ "My weight is: " + weight +" and my color is: " +color;
    }

    @Override
    public void changeSeason()
    {
        setSeason(nextSomeSeason());
        Season season = getCurrentSeason();
        state = "";
        switch (season)
        {
            case WINTER:
                color = Color.WHITE;
                state = "I am migrating south. ";
                break;

            case SPRING:
                color = Color.BROWN;
                break;

            case SUMMER:
                color = Color.BROWN;
                state = "I am migrating north. ";
                break;

            case FALL:
                break;
        }

    }
        // TODO: Implement.

}

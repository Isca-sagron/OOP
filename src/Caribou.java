public class Caribou extends Animal
{

    Caribou(int weight, Season season)
    {
        // TODO: Implement.
        super(weight=0, season = null,Color.BROWN);
    }

    @Override
    public String toString()
    {
        // TODO: Implement.
        return "Caribou: " +state+ "My weight is: " + weight +"and my color is: " +color;
    }

    @Override
    public void changeSeason()
    {
        switch (nextSomeSeason())
        {
            case WINTER:
                color = Color.YELLOW;
                state = "I am migrating south. ";
                break;

            case SPRING:
                color = Color.BROWN;
                break;

            case SUMMER:
                state = "I am migrating north. ";
                break;

            case FALL:
                break;
        }

    }
        // TODO: Implement.

}

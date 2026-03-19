package Ex1;

public class OliveTree extends Tree
{

    OliveTree(int height, Season season)
    {
        // TODO: Implement.
        super(height,season,Color.GREEN);
    }

    @Override
    public String toString()
    {
        // TODO: Implement.
        if (season == Season.FALL)
            state = "I give fruit. ";
        return "Olive tree. " +state+ "My height is: "+height+ " and my color is: " +leavesColor;
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
                height=height + 5;
                break;
            case SPRING:
                height=height + 10;
                break;
            case SUMMER:
                height=height + 10;
                break;
            case FALL:
                height=height + 5;
                state = "I give fruit. ";
                break;
        }
        // TODO: Implement.

    }
}

package Ex1;

public class FigTree extends Tree
{
    FigTree(int height, Season season)
    {
        // TODO: Implement.
        super(height,season,Color.YELLOW);
    }

    @Override
    public String toString()
    {
        // TODO: Implement.
        if (leavesColor == null)
            return "Fig tree. My height is: " +height+ " and I have no leaves";
        if (season == Season.SUMMER)
            state = "I give fruit. ";
        return "Fig tree. "+state+ "My height is: " +height+ " and my color is: " + leavesColor;

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
                height = height + 20;
                leavesColor = null;
                break;
            case SPRING:
                height = height + 30;
                leavesColor = Color.GREEN;
                break;
            case SUMMER:
                height = height + 30;
                leavesColor = Color.GREEN;
                state = "I give fruit. ";

                break;
            case FALL:
                height = height + 20;
                leavesColor = Color.YELLOW;
                break;
        }

    }
        // TODO: Implement.

}

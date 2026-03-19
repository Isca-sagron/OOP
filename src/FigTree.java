import javax.lang.model.element.NestingKind;

public class FigTree extends Tree
{


    FigTree(int height, Season season)
    {
        // TODO: Implement.
        super(height,season=null,null);
    }

    @Override
    public String toString()
    {
        // TODO: Implement.
        if (leavesColor == null)
            return "Fig tree. My height is: " +height+ "and I have no leaves";
        else
            return "Fig tree. My height is: " +height+ "and my color is: " + leavesColor;

    }
    @Override
    public void changeSeason()
    {
        switch (nextSomeSeason())
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
                break;
            case FALL:
                height = height + 20;
                leavesColor = Color.YELLOW;
                break;
        }

    }
        // TODO: Implement.
    }
}

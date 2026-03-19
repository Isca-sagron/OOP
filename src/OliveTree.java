public class OliveTree extends Tree
{


    OliveTree(int height, Season season)
    {
        // TODO: Implement.
        super(0,null,Color.GREEN);
    }

    @Override
    public String toString()
    {
        // TODO: Implement.
        return "Olive tree. " +state+ "My height is: "+height+ "and my color is: " +leavesColor;
    }
    @Override
    public void changeSeason()
    {
        switch ( nextSomeSeason())
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

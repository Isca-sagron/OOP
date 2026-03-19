public class Bear extends Animal
{

    Bear(int weight, Season season)
    {
        // TODO: Implement.
        super(0,null,null);
    }
    @Override
    public String toString()
    {
        // TODO: Implement.
        return "Bear."+state+ " My weight is: " +weight+ "and my color is: " +color;
    }
    @Override
    public void changeSeason()
    {
        // TODO: Implement.
    }
}

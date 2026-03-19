package Ex1;

import java.util.Arrays;

public class SeasonUtil
{
    public static void sort(Comparable[] comparable)
    {
        Arrays.sort(comparable);
    }

    public static void sortSeasonable(Comparable[] seasonables)
    {
        sort(seasonables);
    }

    public static String reportAll(Seasonable[] seasonables)
    {
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < seasonables.length; i++)
        {
            sb.append(seasonables[i].toString());
            if (i < seasonables.length - 1)
            {
                sb.append("\n");
            }
        }

        return sb.toString();
    }
}
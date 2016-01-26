import org.junit.Test;
import org.mockito.Mockito;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

public class SingleComponentTester
{
    @Test
    public void testILogger()
    {
        ILogger logger = mock(ILogger.class);

        int[] logged = { 0, 0, 0 };
        Mockito.doAnswer(invocation -> {
            logged[0]++;
            return null;
        }).when(logger).log(eq(LoggingType.ERROR), anyString());
        Mockito.doAnswer(invocation -> {
            logged[1]++;
            return null;
        }).when(logger).log(eq(LoggingType.WARNING), anyString());

        Mockito.doAnswer(invocation -> {
            logged[2]++;
            return null;
        }).when(logger).log(eq(LoggingType.INFO), anyString());

        logger.log(LoggingType.ERROR, "seriousError");
        logger.log(LoggingType.ERROR, "seriousError2");
        logger.log(LoggingType.WARNING, "wrn");
        logger.log(LoggingType.INFO, "succesfully done");
        logger.log(LoggingType.INFO, "done");
        logger.log(LoggingType.INFO, "done again");

        assertEquals(2, logged[0]);
        assertEquals(1, logged[1]);
        assertEquals(3, logged[2]);
        verify(logger, atLeastOnce()).log(eq(LoggingType.ERROR), anyString());
        verify(logger, times(1)).log(eq(LoggingType.WARNING), anyString());
        verify(logger, times(3)).log(eq(LoggingType.INFO), anyString());
    }


    @Test
    public void testProductionLineMover() {
        IProductionLineMover lineMover = mock(IProductionLineMover.class);

        lineMover.moveProductionLine(MovingDirection.FORWARD);
        lineMover.moveProductionLine(MovingDirection.TO_SCAN);
        lineMover.moveProductionLine(MovingDirection.FORWARD);
        lineMover.moveProductionLine(MovingDirection.FORWARD);

        verify(lineMover, times(3)).moveProductionLine(eq(MovingDirection.FORWARD));
        verify(lineMover, times(1)).moveProductionLine(eq(MovingDirection.TO_SCAN));
        verify(lineMover, never()).moveProductionLine(eq(MovingDirection.BACK));
    }

    @Test
    public void testConstructionRecipe()
    {
        ConstructorRecipe receipe = mock(ConstructorRecipe.class);
        final String nameOfObject = "Fiat 126P";
        when(receipe.getName()).thenReturn(nameOfObject);
        assertEquals(nameOfObject, receipe.getName());
    }

    @Test
    public void testIConstructionRecipeCreator()
    {
        ConstructorRecipe receipe = mock(ConstructorRecipe.class);
        final String nameOfObject = "Fiat 126P";
        when(receipe.getName()).thenReturn(nameOfObject);

        final int numberOfObjectsToProduce = 10;
        IConstructionReceipeCreator receipeCreator = mock(IConstructionReceipeCreator.class);
        when(receipeCreator.getNumberOfElementsToProduce()).thenReturn(numberOfObjectsToProduce);

        assertEquals(numberOfObjectsToProduce, receipeCreator.getNumberOfElementsToProduce());
        assertEquals(nameOfObject, receipe.getName());
    }

    @Test
    public void testObjectConstructor()
    {
        ConstructorRecipe receipe = mock(ConstructorRecipe.class);
        IObjectsConstructor constructor = mock(IObjectsConstructor.class);
        when(constructor.buildObjectFromRecipe(receipe)).thenReturn(true);

        assertEquals(true, constructor.buildObjectFromRecipe(receipe));
        verify(constructor, times(1)).buildObjectFromRecipe(receipe);
    }
}


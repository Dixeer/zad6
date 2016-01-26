import org.junit.Before;
import org.junit.Test;

import static org.mockito.Mockito.*;


public class EndToEndTests
{
    IObjectsConstructor objectsConstructor;
    IConstructionReceipeCreator receipeCreator;
    ILogger logger;
    IProductionLineMover lineMover;
    ConstructorRecipe receipe;
    ControllerTemplateMethod controller;

    final String nameOfObject = "UJ's first car";
    int numberOfObjectsToConstruct = 1;

    @Before
    public void setUpTests() {
        objectsConstructor = mock(IObjectsConstructor.class);
        receipeCreator = mock(IConstructionReceipeCreator.class);
        logger = mock(ILogger.class);
        lineMover = mock(IProductionLineMover.class);
        receipe = mock(ConstructorRecipe.class);
    }

    @Test
    public void testConstructionOfSingleObject() {
        final boolean wasSuccessfull = true;
        when(receipe.getName()).thenReturn(nameOfObject);
        when(receipeCreator.getBuildingRecipe()).thenReturn(receipe);
        when(receipeCreator.getNumberOfElementsToProduce()).thenReturn(numberOfObjectsToConstruct);
        when(lineMover.moveProductionLine(MovingDirection.FORWARD)).thenReturn(wasSuccessfull);
        when(objectsConstructor.buildObjectFromRecipe(receipe)).thenReturn(wasSuccessfull);
        controller = new ControllerWithDependencyInjection(objectsConstructor, receipeCreator, logger, lineMover);

        controller.realize();

        verify(receipe, times(1)).getName();
        verify(receipeCreator, times(1)).getBuildingRecipe();
        verify(objectsConstructor, times(1)).buildObjectFromRecipe(receipe);
        verify(logger, times(1)).log(eq(LoggingType.INFO), anyString());
        verify(lineMover, times(2)).moveProductionLine(MovingDirection.FORWARD);
    }

    @Test
    public void testConstructionFailure_MovingProductionLineFailed() {
        when(receipe.getName()).thenReturn(nameOfObject);
        when(receipeCreator.getBuildingRecipe()).thenReturn(receipe);
        when(receipeCreator.getNumberOfElementsToProduce()).thenReturn(numberOfObjectsToConstruct);
        when(lineMover.moveProductionLine(MovingDirection.FORWARD)).thenThrow(new RuntimeException("Can't move ProductionLine!"));
        controller = new ControllerWithDependencyInjection(objectsConstructor, receipeCreator, logger, lineMover);

        controller.realize();

        verify(receipe, times(1)).getName();
        verify(receipeCreator, times(1)).getBuildingRecipe();
        verify(objectsConstructor, never()).buildObjectFromRecipe(receipe);
        verify(logger, never()).log(eq(LoggingType.INFO), anyString());
        verify(lineMover, times(1)).moveProductionLine(MovingDirection.FORWARD);
    }

    @Test
    public void testConstructionFailure_ObjectConstructionFailed() {
        boolean wasSuccessfull = true;
        when(receipe.getName()).thenReturn(nameOfObject);
        when(receipeCreator.getBuildingRecipe()).thenReturn(receipe);
        when(receipeCreator.getNumberOfElementsToProduce()).thenReturn(numberOfObjectsToConstruct);
        when(lineMover.moveProductionLine(MovingDirection.FORWARD)).thenReturn(wasSuccessfull);
        when(objectsConstructor.buildObjectFromRecipe(receipe)).thenReturn(false);
        when(lineMover.moveProductionLine(MovingDirection.TO_SCAN)).thenReturn(wasSuccessfull);
        controller = new ControllerWithDependencyInjection(objectsConstructor, receipeCreator, logger, lineMover);

        controller.realize();

        verify(receipe, times(1)).getName();
        verify(receipeCreator, times(1)).getBuildingRecipe();
        verify(objectsConstructor, times(1)).buildObjectFromRecipe(receipe);
        verify(logger, times(1)).log(eq(LoggingType.WARNING), anyString());
        verify(lineMover, times(1)).moveProductionLine(MovingDirection.FORWARD);
        verify(lineMover, times(1)).moveProductionLine(MovingDirection.TO_SCAN);
    }

    @Test
    public void testConstructionFailure_ObjectConstructionFailedThenMovingToScanAlsoFailed() {
        boolean wasSuccessfull = true;
        when(receipe.getName()).thenReturn(nameOfObject);
        when(receipeCreator.getBuildingRecipe()).thenReturn(receipe);
        when(receipeCreator.getNumberOfElementsToProduce()).thenReturn(numberOfObjectsToConstruct);
        when(lineMover.moveProductionLine(MovingDirection.FORWARD)).thenReturn(wasSuccessfull);
        when(objectsConstructor.buildObjectFromRecipe(receipe)).thenReturn(false);
        when(lineMover.moveProductionLine(MovingDirection.TO_SCAN)).thenThrow(new RuntimeException("Can't move not constructed car to scan, scan is full!"));
        controller = new ControllerWithDependencyInjection(objectsConstructor, receipeCreator, logger, lineMover);

        controller.realize();

        verify(receipe, times(1)).getName();
        verify(receipeCreator, times(1)).getBuildingRecipe();
        verify(objectsConstructor, times(1)).buildObjectFromRecipe(receipe);
        verify(logger, times(1)).log(eq(LoggingType.WARNING), anyString());
        verify(logger, times(1)).log(eq(LoggingType.ERROR), anyString());
        verify(lineMover, times(1)).moveProductionLine(MovingDirection.FORWARD);
        verify(lineMover, times(1)).moveProductionLine(MovingDirection.TO_SCAN);
    }
}

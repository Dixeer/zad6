
public class ControllerWithDependencyInjection extends ControllerTemplateMethod {

    ILogger logger;
    IObjectsConstructor constructor;
    IConstructionReceipeCreator creator;
    IProductionLineMover productionLineMover;
    ConstructorRecipe receipe;

    public ControllerWithDependencyInjection
            (IObjectsConstructor constructor,
             IConstructionReceipeCreator creator,
             ILogger logger, IProductionLineMover lineMover)
    {
        this.creator = creator;
        this.logger = logger;
        this.constructor = constructor;
        productionLineMover = lineMover;
        receipe = this.creator.getBuildingRecipe();
    }

    @Override
    int howMany()
    {
        return creator.getNumberOfElementsToProduce();
    }

    @Override
    String getProductName()
    {
        return (String) receipe.getName();
    }

    @Override
    boolean next()
    {
        try{
            return productionLineMover.moveProductionLine
                    (MovingDirection.FORWARD);
        } catch (RuntimeException e)
        {
            return false;
        }
    }

    @Override
    boolean scan()
    {
        try {
            return productionLineMover.moveProductionLine
                    (MovingDirection.TO_SCAN);
        } catch (RuntimeException e) {
            return false;
        }
    }

    @Override
    boolean build()
    {
        return constructor.buildObjectFromRecipe(receipe);
    }

    @Override
    void tropic(String message)
    {
        logger.log(LoggingType.WARNING, message);
    }

    @Override
    void inform(String message)
    {
        logger.log(LoggingType.INFO, message);
    }

    @Override
    void errors(String message)
    {
        logger.log(LoggingType.ERROR, message);
    }
}

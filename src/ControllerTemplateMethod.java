
public abstract class ControllerTemplateMethod
{
    abstract int howMany();
    abstract boolean next();
    abstract boolean scan();
    abstract boolean build();
    abstract String getProductName();
    abstract void tropic(String message);
    abstract void inform(String message);
    abstract void errors(String message);

    public void realize() {
        String name = getProductName();
        if (next())
        {
            for (int i = 0; i < howMany(); ++i)
            {
                if (build())
                {
                    inform(name + " created.");
                    next();
                } else
                {
                    tropic(name + " was scanned.");
                    if (!scan())
                        errors(name + " was not created.");
                }
            }
        }
    }
}
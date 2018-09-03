package org.pipservices.container.example;

public class Program {
	
	public static void main(String[] args)
    {
        try
        {
        	DummyProcess process = new DummyProcess();
            process.runWithArgumentsOrConfigFile(null, args, "");

        }
        catch (Exception ex)
        {
        	System.err.println(ex);
        }
    }
}
package com.nokia.jsimple;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Run the Tangible Java -> C# converter.
 */
@Mojo(name = "convert", defaultPhase = LifecyclePhase.PREPARE_PACKAGE)
public class JavaToCSharpConverterMojo extends AbstractMojo {

    /**
     * Java main source directory.  Defaults to ${project.build.sourceDirectory}.
     */
    @Parameter(defaultValue = "${project.build.sourceDirectory}", property = "sourceDirectory", required = false)
    private File sourceDirectory;

    /**
     * Directory for generated C# for main source.  Defaults to ${project.basedir}/c#.
     */
    @Parameter(defaultValue = "${project.basedir}/c#", property = "outputDirectory", required = false)
    private File outputDirectory;

    /**
     * Java test source directory.  Defaults to ${project.build.testSourceDirectory}.  If this directory doesn't exist,
     * then no test sources are converted.
     */
    @Parameter(defaultValue = "${project.build.testSourceDirectory}", property = "testSourceDirectory", required = false)
    private File testSourceDirectory;

    /**
     * Directory for generated C# for test source.  Defaults to ${project.basedir}/c#-test.
     */
    @Parameter(defaultValue = "${project.basedir}/c#-test", property = "testOutputDirectory", required = false)
    private File testOutputDirectory;

    /**
     * Directory that contains the converter.  Java to C# Converter.exe must be in this directory.
     */
    @Parameter(property = "converterDirectory", required = true)
    private File converterDirectory;

    /**
     * Path to converter settings file (which normally has a .dat extension).
     */
    @Parameter(property = "converterSettings", required = true)
    private File converterSettings;

    /**
     * If true, don't do anything.  Defaults to the skipConversion global property.  Thus "mvn -DskipConversion" will
     * skip the Java -> C# conversion.
     */
    @Parameter(defaultValue = "${skipConversion}", property = "skipConversion", required = false)
    private boolean skip;

    public void execute() throws MojoExecutionException {
        if (skip)
            return;

        deleteGeneratedDirectories(outputDirectory);

        String[] convertSourceArgs = new String[]{
                new File(converterDirectory, "Java to C# Converter.exe").getPath(),
                sourceDirectory.getAbsolutePath(),
                outputDirectory.getAbsolutePath(),
                converterSettings.getAbsolutePath()
        };
        convert(convertSourceArgs);

        if (testSourceDirectory.exists()) {
            deleteGeneratedDirectories(testOutputDirectory);

            String[] convertTestArgs = new String[]{
                    new File(converterDirectory, "Java to C# Converter.exe").getPath(),
                    testSourceDirectory.getAbsolutePath(),
                    testOutputDirectory.getAbsolutePath(),
                    converterSettings.getAbsolutePath()
            };
            convert(convertTestArgs);
        }
    }

    private void convert(String[] converterArgs) throws MojoExecutionException {
        try {
            StringBuilder command = new StringBuilder();
            for (String arg : converterArgs)
                command.append(arg + " ");
            getLog().debug("Executing: " + command);

            Process converter = Runtime.getRuntime().exec(converterArgs, null, converterDirectory);

            BufferedReader reader = new BufferedReader(new InputStreamReader(converter.getInputStream()));
            String inputLine;
            while ((inputLine = reader.readLine()) != null)
                getLog().info(inputLine);
            reader.close();
        } catch (IOException e) {
            throw new MojoExecutionException("IO exception when running converter", e);
        }
    }

    /**
     * Delete the contents of the specified directory that appear to be generated via the converter.  The rule used here
     * is that all subdirectories of the specified directory are deleted, except for Bin, obj, Properties, and
     * nontranslated.
     *
     * @param path directory/file to delete
     * @throws MojoExecutionException
     */
    private static void deleteGeneratedDirectories(File path) throws MojoExecutionException {
        if (path.isDirectory()) {
            for (File child : path.listFiles()) {
                String name = child.getName();

                if (child.isDirectory() && !name.equals("Properties") && !name.equals("nontranslated") &&
                        !name.equalsIgnoreCase("Bin") && !name.equalsIgnoreCase("obj"))
                    deleteRecursively(child);
            }
        }
    }

    /**
     * Delete the specified directory/file.  If there's an error deleting anything, an exception is thrown.
     *
     * @param path directory/file to delete
     * @throws MojoExecutionException
     */
    private static void deleteRecursively(File path) throws MojoExecutionException {
        if (path.isDirectory()) {
            for (File child : path.listFiles())
                deleteRecursively(child);
        }

        if (!path.delete())
            throw new MojoExecutionException("Failed to delete this file/directory: " + path.getAbsolutePath());
    }
}

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
import java.util.ArrayList;
import java.util.Arrays;

/**
 * Run the Tangible Java -> C# converter.
 */
@Mojo(name = "convert", defaultPhase = LifecyclePhase.PREPARE_PACKAGE)
public class JavaToObjectiveCMojo extends AbstractMojo {

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
     * Path to converter settings file (which normally has a .dat extension). MIKE:  You can get rid of this.  But
     * instead probably need to add settings for things that the caller may want to customize in the conversion.
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
                sourceDirectory.getAbsolutePath(),
                outputDirectory.getAbsolutePath(),
                converterSettings.getAbsolutePath()
        };
        convert(convertSourceArgs);

        if (testSourceDirectory.exists()) {
            deleteGeneratedDirectories(testOutputDirectory);

            String[] convertTestArgs = new String[]{
                    testSourceDirectory.getAbsolutePath(),
                    testOutputDirectory.getAbsolutePath(),
                    converterSettings.getAbsolutePath()
            };
            convert(convertTestArgs);
        }
    }

    private void convert(String[] converterArgs) throws MojoExecutionException {
        try {
            ArrayList<String> completeArgs = new ArrayList<String>();

            completeArgs.add(new File(converterDirectory, "Java to C# Converter.exe").getPath());

            completeArgs.addAll(Arrays.asList(converterArgs));

            StringBuilder command = new StringBuilder();
            for (String arg : completeArgs)
                command.append(arg + " ");
            getLog().debug("Executing: " + command);

            Process converter = Runtime.getRuntime().exec(completeArgs.toArray(new String[completeArgs.size()]), null,
                    converterDirectory);

            // MIKE - You can probably delete these lines.  But you may want to do something similar to filter /
            // transform the j2objc conversion messages, getting rid of "noise" but making sure errors/warnings get
            // caught
            BufferedReader reader = new BufferedReader(new InputStreamReader(converter.getInputStream()));
            String inputLine;
            while ((inputLine = reader.readLine()) != null) {
                // Don't output the pass number stuff
                if (inputLine.endsWith(" pass..."))
                    continue;

                getLog().info(inputLine);
            }
            reader.close();
        } catch (IOException e) {
            throw new MojoExecutionException("IO exception when running j2objc", e);
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

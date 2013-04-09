package com.nokia.jsimple;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Run MSBuild.
 */
@Mojo(name = "compile", defaultPhase = LifecyclePhase.COMPILE)
public class MSBuildMojo extends AbstractMojo {

    /**
     * Path to where .NET is installed.  This directory should contain MSBuild.exe.  Defaults to the value of the
     * dotNETDirectory property.
     */
    @Parameter(property = "dotNETDirectory", defaultValue = "${dotNETDirectory}", required = false)
    private File dotNETDirectory;

    /**
     * Path to project or solution .sln) file to build.  Defaults to msbuildProject property.
     */
    @Parameter(property = "project", defaultValue = "${msbuildProject}", required = false)
    private File project;

    /**
     * Configuration to build (Release, Debug, etc.).  Defaults to Release.
     */
    @Parameter(property = "configuration", defaultValue = "Release", required = false)
    private String configuration;

    /**
     * MSBuild output verbosity.  You can specify the following verbosity levels: q[uiet], m[inimal], n[ormal],
     * d[etailed], and diag[nostic].  "normal" is the MSBuild default, but we default to "minimal" instead as that's
     * generally better for batch builds.
     */
    @Parameter(property = "verbosity", defaultValue = "minimal", required = false)
    private String verbosity;

    /**
     * Target(s) to build in the project/solution.  Specify each target separately, or use a semicolon or comma to
     * separate multiple targets (e.g., "Resources;Compile".  Defaults to Rebuild.
     */
    @Parameter(property = "target", defaultValue = "Rebuild", required = false)
    private String target;

    public void execute() throws MojoExecutionException, MojoFailureException {
        String[] args = new String[]{
                new File(dotNETDirectory, "MSBuild.exe").getPath(),
                "/t:" + target,
                "/property:Configuration=" + configuration,
                "/verbosity:" + verbosity,
                project.getAbsolutePath()
        };

        try {
            StringBuilder command = new StringBuilder(new File(dotNETDirectory, "MSBuild.exe").getPath());

            command.append(" /t:" + target);
            command.append(" /property:Configuration=" + configuration);
            if (!verbosity.equals("normal"))
                command.append(" /verbosity:" + verbosity);
            command.append(project.getAbsolutePath());

            getLog().debug("Executing: " + command);

            Process msbuild = Runtime.getRuntime().exec(args, null, null);

            BufferedReader reader = new BufferedReader(new InputStreamReader(msbuild.getInputStream()));
            String inputLine;
            while ((inputLine = reader.readLine()) != null) {
                if (inputLine.contains(": error "))
                    getLog().error(inputLine);
                else if (inputLine.contains(": warning "))
                    getLog().warn(inputLine);
                else getLog().info(inputLine);
            }
            reader.close();

            int exitCode = msbuild.exitValue();
            if (exitCode > 0)
                throw new MojoFailureException("MSBuild failed with exit code " + exitCode);
        } catch (IOException e) {
            throw new MojoExecutionException("IO exception when running MSBuild", e);
        }
    }

}

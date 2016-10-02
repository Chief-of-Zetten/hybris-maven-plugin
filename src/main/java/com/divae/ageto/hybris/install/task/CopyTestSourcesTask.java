package com.divae.ageto.hybris.install.task;

import java.io.File;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.divae.ageto.hybris.install.task.copy.CopyDirectoryContentToDirectoryTask;

/**
 * @author Klaus Hauschild
 */
public class CopyTestSourcesTask extends AbstractWorkDirectoryTask {

    private static final String SKIPTESTS = "skipTestSources";
    private static final Logger LOGGER    = LoggerFactory.getLogger(CopyTestSourcesTask.class);
    private final File fromDirectory;
    private final File toDirectory;

    public CopyTestSourcesTask(final File fromDirectory, final File toDirectory) {
        this.fromDirectory = fromDirectory;
        this.toDirectory = toDirectory;
    }

    public static void enableTestSources(final TaskContext taskContext) {
        taskContext.setParameter(SKIPTESTS, true);
    }

    /*Returns whether the tests sources with the unit tests of the hybris suite will be copied and executed while
      restructuring the hybris plugins or not
      @param    taskContext the context of the current running task
    * */
    public static boolean getTestSourcesEnabled(final TaskContext taskContext) {
        if (taskContext.getParameter(SKIPTESTS) != null) {
            return (boolean) taskContext.getParameter(SKIPTESTS);
        }
        return false;
    }

    @Override
    protected void execute(final TaskContext taskContext, final File workDirectory) {
        // TODO khauschild: prevent empty directories

        if (getTestSourcesEnabled(taskContext) && fromDirectory.exists()) {
            new CopyDirectoryContentToDirectoryTask(fromDirectory, toDirectory).execute(taskContext);
        }
    }

}

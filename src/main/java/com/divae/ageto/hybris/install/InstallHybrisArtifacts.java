package com.divae.ageto.hybris.install;

import com.divae.ageto.hybris.install.task.TaskChainTask;
import com.divae.ageto.hybris.install.task.TaskContext;
import com.divae.ageto.hybris.version.HybrisVersion;

import java.io.File;

/**
 * @author Klaus Hauschild
 */
public class InstallHybrisArtifacts {

    private final TaskContext   taskContext;
    private final TaskChainTask installTasks;

    InstallHybrisArtifacts(final File hybrisDirectory) {
        final HybrisVersion hybrisVersion = HybrisVersion.of(hybrisDirectory);
        taskContext = new TaskContext(hybrisVersion, hybrisDirectory);
        installTasks = new TaskChainTask(InstallStrategy.getInstallTasks(taskContext));
    }

    void execute() {
        installTasks.execute(taskContext);
    }

}

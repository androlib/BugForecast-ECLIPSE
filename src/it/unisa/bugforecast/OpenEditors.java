package it.unisa.bugforecast;

import java.io.File;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.ui.IEditorDescriptor;
import org.eclipse.ui.IEditorRegistry;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.FileEditorInput;

public class OpenEditors {

	public static void openFileInEditor(File file) throws PartInitException {
		
		IWorkspace workspace = ResourcesPlugin.getWorkspace();
		IEditorRegistry editorRegistry = PlatformUI.getWorkbench().getEditorRegistry();
		IWorkbenchPage page = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
		IPath location = Path.fromOSString(file.getAbsolutePath());
		IFile ifile = workspace.getRoot().getFileForLocation(location);
		FileEditorInput fileEditorInput = new FileEditorInput(ifile);
		IPath filePath = ifile.getFullPath();
		String filePathString = filePath.toString();

		IEditorDescriptor defaultEditor = editorRegistry.getDefaultEditor(filePathString);
		String defaultEditorId = defaultEditor.getId();

		page.openEditor(fileEditorInput, defaultEditorId);

	}

}

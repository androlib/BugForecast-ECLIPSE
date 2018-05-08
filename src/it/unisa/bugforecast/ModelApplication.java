package it.unisa.bugforecast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;

import javax.swing.ImageIcon;
import javax.swing.JOptionPane;

import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.DirectoryDialog;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.forms.widgets.Form;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.Section;
import org.eclipse.ui.part.ViewPart;

import weka.core.Instances;

/**
 * aleso
 */
public class ModelApplication extends ViewPart {

	private Form form;
	private FormToolkit toolkit;
	private Button applyOffLinelButton;
	private Label testFileLabel;
	private Text testFileText;
	private Text outputFolderText;
	private Label outputFolderLabel;
	private String workspace;
	private String nameProject;
	private ArrayList<String> namesClasses;
	private String binPath;
	private MultiSelectionCombo comboClass;
	private Combo comboProjects;
	private Label comboProjectsLabel;
	private Button selectIntProjectButton;

	public ModelApplication() {
		workspace = ResourcesPlugin.getWorkspace().getRoot().getLocation().toString();
		nameProject = null;
		namesClasses = new ArrayList<String>();
		binPath = null;
	}

	@Override
	public void createPartControl(Composite parent) {
		// TODO Auto-generated method stub
		toolkit = new FormToolkit(parent.getDisplay());

		form = toolkit.createForm(parent);
		GridLayout layout = new GridLayout(4, false);
		form.getBody().setLayout(layout);

		createOnLinelSection();
		
		createOnLineButton();

		createOffLinelSection();

		createOffLineButton();

		createApplyButton();
	}

	@Override
	public void setFocus() {
		// TODO Auto-generated method stub
		form.setFocus();
	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub
		toolkit.dispose();
		super.dispose();
	}

	public void createOffLineButton() {
		GridData gridData = new GridData();
		gridData.horizontalAlignment = GridData.FILL;
		gridData.horizontalSpan = 3;

		testFileLabel = new Label(form.getBody(), SWT.BORDER);
		testFileLabel.setText("Test File");
		testFileText = new Text(form.getBody(), SWT.BORDER);
		testFileText.setLayoutData(gridData);
		testFileText.setEditable(false);
		testFileText.addMouseListener(new MouseListener() {
			@Override
			public void mouseDoubleClick(MouseEvent e) {
			}

			@Override
			public void mouseDown(MouseEvent e) {
				DirectoryDialog directoryDialog = new DirectoryDialog(form.getShell());
				directoryDialog.setFilterPath("");
				directoryDialog.setMessage("Please select a directory and click OK.");

				String directory = directoryDialog.open();
				if (directory != null) {
					testFileText.setText(directory);
				}

			}

			@Override
			public void mouseUp(MouseEvent e) {
			}
		});

		outputFolderLabel = new Label(form.getBody(), SWT.BORDER);
		outputFolderLabel.setText("Output Folder");
		outputFolderText = new Text(form.getBody(), SWT.BORDER);
		outputFolderText.setLayoutData(gridData);
		outputFolderText.setEditable(false);
		outputFolderText.addMouseListener(new MouseListener() {
			@Override
			public void mouseDoubleClick(MouseEvent e) {
			}

			@Override
			public void mouseDown(MouseEvent e) {
				DirectoryDialog directoryDialog = new DirectoryDialog(form.getShell());

				directoryDialog.setFilterPath("");
				directoryDialog.setMessage("Please select a directory and click OK.");

				String outputFile = directoryDialog.open();
				if (outputFile != null) {
					outputFolderText.setText(outputFile);
				}
			}

			@Override
			public void mouseUp(MouseEvent e) {
			}
		});

	}

	private void createApplyButton() {
		GridData gridData = new GridData();
		gridData.horizontalAlignment = GridData.FILL;
		gridData.horizontalSpan = 4;
		applyOffLinelButton = toolkit.createButton(form.getBody(), "Apply Model", SWT.NULL);

		applyOffLinelButton.addMouseListener(new MouseListener() {
			@Override
			public void mouseDoubleClick(MouseEvent e) {
			}

			@Override
			public void mouseDown(MouseEvent e) {

				applyOffLinelButton.addMouseListener(new MouseListener() {
					@Override
					public void mouseDoubleClick(MouseEvent e) {
					}

					@Override
					public void mouseDown(MouseEvent e) {

						if (testFileText.getText().isEmpty() || outputFolderText.getText().isEmpty()) {
							JOptionPane.showMessageDialog(null, "Some input data are missing.", "Attention",
									JOptionPane.INFORMATION_MESSAGE, new ImageIcon("icons//cartella.png"));
						} else {

							File fInput = new File(testFileText.getText());
							File fOutput = new File("file.txt");
							CreateTask ct = new CreateTask(fInput, fOutput);
							ct.startTask();
							ReaderTxt rt;
							CreateCSV csv;
							try {

								rt = new ReaderTxt(new FileReader("file.txt"));
								ArrayList<MetricClass> list = rt.execute();
								csv = new CreateCSV("testSet.csv", list);
								csv.execute();

								
								CSVtoARFF.convert("testSet", "testSet.csv");
								BufferedReader reader1 = new BufferedReader(new FileReader("testSet.arff"));

								Instances test = new Instances(reader1);
								reader1.close();

								Model model = getModel();
								if(model == null) {
									JOptionPane.showMessageDialog(null, "Please, create or load a model first.", "Attention",
											JOptionPane.INFORMATION_MESSAGE, new ImageIcon("icons//cartella.png"));
									return;
								}
								model.setpTestSet(test);
								model.buildAndEvaluate();
								model.generateFilePredictions(outputFolderText.getText());
								deleteFiles();

							} catch (FileNotFoundException a) {
								// TODO Auto-generated catch block
								a.printStackTrace();
							} catch (IOException e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
							}
						}

					}

					@Override
					public void mouseUp(MouseEvent e) {
					}
				});

			}

			@Override
			public void mouseUp(MouseEvent e) {
			}
		});

		applyOffLinelButton.setLayoutData(gridData);
	}

	public void createOnLineButton() {
		File fInput = new File(workspace);
		String a[] = fInput.list();
		ArrayList<String> nameProjects = new ArrayList<String>();
		for (int i = 0; i < a.length; i++) {
			if (Character.isUpperCase(a[i].charAt(0))) {
				nameProjects.add(a[i]);
			}
		}
		String items[] = nameProjects.toArray(new String[nameProjects.size()]);

		comboProjectsLabel = new Label(form.getBody(), SWT.BORDER);
		comboProjectsLabel.setText("Eclipse Projects");

		comboProjects = new Combo(form.getBody(), SWT.CENTER | SWT.READ_ONLY | SWT.DROP_DOWN);
		comboProjects.setItems(items);
		GridData gridData2 = new GridData();
		gridData2.horizontalAlignment = GridData.FILL;
		gridData2.horizontalSpan = 2;
		comboProjects.setLayoutData(gridData2);

		GridData gridData3 = new GridData();
		gridData3.horizontalAlignment = GridData.FILL;

		selectIntProjectButton = toolkit.createButton(form.getBody(), "Select Classes and Apply Model", SWT.NULL);

		selectIntProjectButton.setLayoutData(gridData3);

		selectIntProjectButton.addMouseListener(new MouseListener() {

			@Override
			public void mouseUp(MouseEvent e) {
				// TODO Auto-generated method stub

			}

			@Override
			public void mouseDown(MouseEvent e) {
				// TODO Auto-generated method stub

			}

			@Override
			public void mouseDoubleClick(MouseEvent e) {
				// TODO Auto-generated method stub

				System.out.println(ResourcesPlugin.getWorkspace().getRoot().getLocation());
				namesClasses = new ArrayList<String>();
				if (comboProjects.getText().equals("")) {
					JOptionPane.showMessageDialog(null, "Please, select a project.", "Attention",
							JOptionPane.INFORMATION_MESSAGE, new ImageIcon("icons//cartella.png"));
					return;
				}
				nameProject = comboProjects.getText();

				projectPathFile();
				binPath = workspace + "\\" + nameProject + "\\bin";
				File bin = new File(binPath);
				listClass(bin);
				String[] classes = namesClasses.toArray(new String[namesClasses.size()]);

				Shell shell = new Shell();
				shell.setLayout(new GridLayout());

				int[] selection = new int[] { 0 };
				comboClass = new MultiSelectionCombo(shell, classes, selection, SWT.NONE);
				comboClass.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, true, false));
				((GridData) comboClass.getLayoutData()).widthHint = 300;

				Button button = new Button(shell, SWT.NONE);
				button.setText("Generate Predictions");
				button.addSelectionListener(new SelectionAdapter() {
					public void widgetSelected(SelectionEvent event) {

						int[] selections = comboClass.getSelections();
						ArrayList<String> selectedClasses = new ArrayList<String>();
						for (int i = 0; i < selections.length; i++) {
							selectedClasses.add(
									binPath + "\\" + namesClasses.get(selections[i]).replaceAll(".java", ".class"));
						}
						CreateTask ct = new CreateTask();
						ct.setfOutput(new File("file.txt"));
						ct.startTask(selectedClasses);
						Model model = getModel();
						if (model == null) {
							JOptionPane.showMessageDialog(null,
									"Please, create or load a model first.", "Attention",
									JOptionPane.INFORMATION_MESSAGE, new ImageIcon("icons//cartella.png"));
							shell.close();
							return;
						}

						else {
							ReaderTxt rt;
							CreateCSV csv;
							try {

								rt = new ReaderTxt(new FileReader("file.txt"));
								ArrayList<MetricClass> list = rt.execute();
								csv = new CreateCSV("testSet.csv", list);
								csv.execute();

								CSVtoARFF.convert("testSet", "testSet.csv");
								BufferedReader reader = new BufferedReader(new FileReader("testSet.arff"));

								Instances test = new Instances(reader);
								reader.close();

								model.setpTestSet(test);
								model.buildAndEvaluate();
								model.generateFilePredictions();
								getPreditions("results.csv");
								deleteFiles();
								shell.close();

								try {
									PlatformUI.getWorkbench().getActiveWorkbenchWindow()
											.getActivePage().showView("result");

								} catch (PartInitException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}

							} catch (FileNotFoundException a) {
								// TODO Auto-generated catch block
								a.printStackTrace();
								shell.close();
							} catch (IOException e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
								shell.close();
							}
						}
					}
				});

				shell.pack();
				shell.open();

			}
		});

	}

	public void createOffLinelSection() {
		GridData gridData = new GridData();
		gridData.horizontalAlignment = GridData.FILL;
		gridData.horizontalSpan = 4;
		Section offLineSection = toolkit.createSection(form.getBody(), Section.DESCRIPTION | Section.TITLE_BAR);
		offLineSection.setText("Off Line Prediction");
		offLineSection.setLayoutData(gridData);
	}

	public void createOnLinelSection() {
		GridData gridData = new GridData();
		gridData.horizontalAlignment = GridData.FILL;
		gridData.horizontalSpan = 4;
		Section onLineSection = toolkit.createSection(form.getBody(), Section.DESCRIPTION | Section.TITLE_BAR);
		onLineSection.setText("On Line Prediction");
		onLineSection.setLayoutData(gridData);
	}

	public void deleteFiles() {
		File f1 = new File("file.txt");
		File f2 = new File("trainingSet.arff");
		File f3 = new File("testSet.csv");
		File f4 = new File("testSet.arff");
		if (f1.exists())
			f1.delete();
		if (f2.exists())
			f2.delete();
		if (f3.exists())
			f3.delete();
		if (f4.exists())
			f4.delete();

	}

	public void projectPathFile() {
		File f = new File("projectPath.txt");
		if (f.exists())
			f.delete();
		try {
			FileWriter fw = new FileWriter("projectPath.txt");
			fw.write(nameProject);
			fw.flush();
			fw.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void listClass(File partenza) {
		File[] list = partenza.listFiles();
		int MAX = list.length;
		for (int i = 0; i < MAX; i++) {
			if (list[i].isDirectory())
				listClass(list[i]);
			else {
				String nome = list[i].toString();
				if (nome.substring(nome.lastIndexOf(".")).equals(".class")) {

					int binPosition = nome.indexOf("\\bin\\");
					namesClasses.add(nome.substring(binPosition + 5).replaceAll(".class", ".java"));
				}
			}
		}

	}

	public void getPreditions(String path) {
		try {
			ReaderCSV rcsv = new ReaderCSV(new FileReader(path));
			ArrayList<MetricClass> mc = rcsv.execute();
			boolean bug = false;
			String info = "These classes could have a bug: ";
			for (MetricClass m : mc) {
				if (m.getBug()) {
					info += m.getName() + " ";
					bug = true;
				}
			}

			if (!bug) {
				info += "none";
			}
			JOptionPane.showMessageDialog(null, info, "Attention", JOptionPane.INFORMATION_MESSAGE,
					new ImageIcon("icons//cartella.png"));

		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public Model getModel() {
		Model model = null;
		File f = new File("Model.model");
		if(!f.exists()) {
			return null;
		}
		try {
			ObjectInputStream in = new ObjectInputStream(new FileInputStream("Model.model"));
			Object o = in.readObject();
			if(o instanceof Model) {
			model = (Model) o;
			}
			else {
				JOptionPane.showMessageDialog(null, "This file isn't a model.", "Attention",
						JOptionPane.INFORMATION_MESSAGE, new ImageIcon("icons//cartella.png"));
			}
			in.close();
			
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (ClassNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		return model;
	}
}

package it.unisa.bugforecast;


import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;



import javax.swing.ImageIcon;
import javax.swing.JOptionPane;



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
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.forms.widgets.Form;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.part.ViewPart;

import weka.core.Instances;

public class Application extends ViewPart {
	protected static final String CLOSED_OPTION = null;
	private Form form;
	private FormToolkit toolkit;
	private Button externalProjectRadio;
	private Button eclipseProjectRadio;
	private Button selectIntProjectButton;
	private Button buildModelButton;
	private MultiSelectionCombo comboClass;
	private Combo comboProjects;
	private Label comboProjectsLabel;
	private Text trainingFileText;
	private Label testFileLabel;
	private Text testFileText;
	private Text outputFolderText;
	private Label outputFolderLabel;
	private Combo classifierComboBox;
	private Label classifierLabel;
	private String workspace;
	private String nameProject;
	private ArrayList<String> namesClasses;
	private String binPath;
	private Model model;

	/**
	 * The constructor.
	 */
	public Application() {
		workspace = "C:\\Users\\aleso\\Desktop\\runtime-EclipseApplication\\";
		nameProject = null;
		namesClasses = new ArrayList<String>();
		binPath = null;
	}

	/**
	 * This is a callback that will allow us to create the viewer and initialize it.
	 */
	public void createPartControl(Composite parent) {
		toolkit = new FormToolkit(parent.getDisplay());

		form = toolkit.createForm(parent);
		GridLayout layout = new GridLayout(4, false);
		form.getBody().setLayout(layout);

		createProjectTypeRadioButtons();

		createStrategiesSection();

		createEclipseProjectButton();
		
		createSubmitButton();

		
		
		

	}

	/**
	 * Passing the focus request to the form.
	 */
	public void setFocus() {
		form.setFocus();
	}

	/**
	 * Disposes the toolkit
	 */
	public void dispose() {
		toolkit.dispose();
		super.dispose();
	}

	public void createProjectTypeRadioButtons() {
		Label projectTypeLabel = new Label(form.getBody(), SWT.NULL);
		projectTypeLabel.setText("Project-Type");
		
		Composite projectTypeRadioButtons = toolkit.createComposite(form.getBody(), SWT.NONE);
		
		externalProjectRadio = toolkit.createButton(projectTypeRadioButtons, "External-Project", SWT.RADIO);
		eclipseProjectRadio = toolkit.createButton(projectTypeRadioButtons, "Eclipse-Project", SWT.RADIO);
		eclipseProjectRadio.setSelection(true);
		GridData gridData = new GridData();
		gridData.horizontalAlignment = GridData.FILL;
		gridData.horizontalSpan = 3;
		projectTypeRadioButtons.setLayout(new GridLayout(3, false));
		projectTypeRadioButtons.setLayoutData(gridData);
		
		externalProjectRadio.addMouseListener(new MouseListener() {
			@Override
			public void mouseDoubleClick(MouseEvent e) {
			}

			@Override
			public void mouseDown(MouseEvent e) {
				testFileLabel.setVisible(true);
				testFileText.setVisible(true);
				selectIntProjectButton.setVisible(false);
				buildModelButton.setVisible(true);
				outputFolderText.setVisible(true);
				outputFolderLabel.setVisible(true);
				comboProjects.setVisible(false);
				comboProjectsLabel.setVisible(false);
			}

			@Override
			public void mouseUp(MouseEvent e) {
			}
		});
		eclipseProjectRadio.addMouseListener(new MouseListener() {
			@Override
			public void mouseDoubleClick(MouseEvent e) {
			}

			@Override
			public void mouseDown(MouseEvent e) {
				testFileLabel.setVisible(false);
				testFileText.setVisible(false);
				selectIntProjectButton.setVisible(true);
				buildModelButton.setVisible(false);
				outputFolderText.setVisible(false);
				outputFolderLabel.setVisible(false);
				comboProjects.setVisible(true);
				comboProjectsLabel.setVisible(true);
				trainingFileText.setText("");
				classifierComboBox.setText("");
				
			}

			@Override
			public void mouseUp(MouseEvent e) {
			}
		});
	}
	
	public void createStrategiesSection() {
		GridData gridData = new GridData();
		gridData.horizontalAlignment = GridData.FILL;
		gridData.horizontalSpan = 3;
		Label trainingFileLabel = new Label(form.getBody(), SWT.BORDER);
		trainingFileLabel.setText("Training File");
		trainingFileText = new Text(form.getBody(), SWT.BORDER);
		trainingFileText.setEditable(false);
		trainingFileText.setLayoutData(gridData);
		trainingFileText.addMouseListener(new MouseListener() {
			@Override
			public void mouseDoubleClick(MouseEvent e) {
			}

			@Override
			public void mouseDown(MouseEvent e) {

				FileDialog fileDialog = new FileDialog(form.getShell(), SWT.MULTI);
				fileDialog.setFilterPath("");
				fileDialog.setFilterExtensions(new String[] { "*.csv" });
				fileDialog.setFilterNames(new String[] { "Comma Separated Values" });

				String dataFile = fileDialog.open();

				if (dataFile != null) {
					String[] selectedFiles = fileDialog.getFileNames();
					StringBuffer stringBuffer = new StringBuffer(fileDialog.getFilterPath() + "\\");
					for (int i = 0; i < selectedFiles.length; i++) {
						stringBuffer.append(selectedFiles[i]);
					}
					trainingFileText.setText(stringBuffer.toString());
				}

			}

			@Override
			public void mouseUp(MouseEvent e) {
			}
		});

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

		String items[] = { "Log", "NB", "RBF", "C45", "Decision Table", "Voting", "Bagging", "Boostring",
				"Random Forest", "CODEP" };

		classifierLabel = new Label(form.getBody(), SWT.BORDER);
		classifierLabel.setText("Classifier");

		classifierComboBox = new Combo(form.getBody(), SWT.CENTER | SWT.READ_ONLY | SWT.DROP_DOWN);
		classifierComboBox.setItems(items);
		GridData gridData2 = new GridData();
		gridData2.horizontalAlignment = GridData.FILL;
		gridData2.horizontalSpan = 2;
		classifierComboBox.setLayoutData(gridData2);

		GridData gridData3 = new GridData();
		gridData3.horizontalAlignment = GridData.FILL;
		Button classifierProperties = toolkit.createButton(form.getBody(), "Properties", SWT.ICON_INFORMATION);
		classifierProperties.setLayoutData(gridData3);

		classifierProperties.addMouseListener(new MouseListener() {
			@Override
			public void mouseDoubleClick(MouseEvent e) {
			}

			@Override
			public void mouseDown(MouseEvent e) {
				Object selected = classifierComboBox.getText();
				String select = selected.toString();

				JOptionPane.showInputDialog("Insert details", select);
			}

			@Override
			public void mouseUp(MouseEvent e) {
			}
		});
	}

	//usa progetto esterno
	private void createSubmitButton() {
		GridData gridData = new GridData();
		gridData.horizontalAlignment = GridData.FILL;
		gridData.horizontalSpan = 4;
		buildModelButton = toolkit.createButton(form.getBody(), "Build Model", SWT.NULL);
		if(eclipseProjectRadio.getSelection()) {
			buildModelButton.setVisible(false);
			testFileLabel.setVisible(false);
			testFileText.setVisible(false);
			outputFolderText.setVisible(false);
			outputFolderLabel.setVisible(false);
			comboProjects.setVisible(true);
			comboProjectsLabel.setVisible(true);
		}
		buildModelButton.addMouseListener(new MouseListener() {
			@Override
			public void mouseDoubleClick(MouseEvent e) {
			}

			@Override
			public void mouseDown(MouseEvent e) {

				buildModelButton.addMouseListener(new MouseListener() {
					@Override
					public void mouseDoubleClick(MouseEvent e) {
					}

					@Override
					public void mouseDown(MouseEvent e) {

						if (trainingFileText.getText().isEmpty() || testFileText.getText().isEmpty()
								|| outputFolderText.getText().isEmpty() || classifierComboBox.getText().isEmpty()) {
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

								boolean convertFlag = CSVtoARFF.convert("trainingSet", trainingFileText.getText());
								if (!convertFlag) {
									JOptionPane.showMessageDialog(null,
											"Please, select a CSV training set with ',' as separator.", "Attention",
											JOptionPane.INFORMATION_MESSAGE, new ImageIcon("icons//cartella.png"));
								}

								else {
									BufferedReader reader = new BufferedReader(new FileReader("trainingSet.arff"));
									Instances training = new Instances(reader);
									reader.close();
									CSVtoARFF.convert("testSet", "testSet.csv");
									BufferedReader reader1 = new BufferedReader(new FileReader("testSet.arff"));
									
									Instances test = new Instances(reader1);
									reader1.close();

									model = new Model(classifierComboBox.getText(), training, test);
									model.buildAndEvaluate();
									model.generateFilePredictions(outputFolderText.getText());
									deleteFiles();
									

								}
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

		buildModelButton.setLayoutData(gridData);
	}
	//usa progetto interno
	public void createEclipseProjectButton() {
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
		
		
		selectIntProjectButton = toolkit.createButton(form.getBody(), "Select Classes", SWT.NULL);
			if(externalProjectRadio.getSelection()) {
				selectIntProjectButton.setVisible(false);
				testFileLabel.setVisible(true);
				testFileText.setVisible(true);
				outputFolderText.setVisible(true);
				outputFolderLabel.setVisible(true);
				comboProjects.setVisible(false);
				comboProjectsLabel.setVisible(false);
			}
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
				namesClasses = new ArrayList<String>();
				if(comboProjects.getText().equals("")) {
					JOptionPane.showMessageDialog(null, "Please, select a project.", "Attention",
							JOptionPane.INFORMATION_MESSAGE, new ImageIcon("icons//cartella.png"));
					return;
				}
				nameProject = comboProjects.getText();
				binPath= workspace + "\\" + nameProject + "\\bin"; 
				File bin = new File(binPath);
				listClass(bin);
				String[] classes = namesClasses.toArray(new String [namesClasses.size()]);
				
				
			    Shell shell = new Shell();
			    shell.setLayout(new GridLayout());
				
				int[] selection = new int[] { 0 };
				comboClass = new MultiSelectionCombo(shell, classes, selection, SWT.NONE);
				comboClass.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, true, false));
			    ((GridData)comboClass.getLayoutData()).widthHint = 300;
			    
			    Button button = new Button(shell, SWT.NONE);
			      button.setText("Generate Predictions");
			      button.addSelectionListener(new SelectionAdapter() {
			         public void widgetSelected(SelectionEvent event) {
			            
			            int [] selections = comboClass.getSelections();
			            ArrayList<String> selectedClasses = new ArrayList<String>();
			            for(int i = 0; i<selections.length; i++) {
			            	selectedClasses.add(binPath+"\\"+namesClasses.get(selections[i]));
			            }
			            CreateTask ct = new CreateTask();
						ct.setfOutput(new File("file.txt"));
						ct.startTask(selectedClasses);
			            if(model == null && (trainingFileText.getText().isEmpty() || trainingFileText.getText().equals(""))) {
							JOptionPane.showMessageDialog(null, "Please, create the model first and insert training file.", "Attention",
									JOptionPane.INFORMATION_MESSAGE, new ImageIcon("icons//cartella.png"));
							shell.close();
							return;
			            }
			            
			            if(model == null) {
			            	if(classifierComboBox.getText().isEmpty()) {
			            		JOptionPane.showMessageDialog(null, "Please, select a classifier.", "Attention",
										JOptionPane.INFORMATION_MESSAGE, new ImageIcon("icons//cartella.png"));
			            		shell.close();
			            		return;
			            	}
			            	ReaderTxt rt;
							CreateCSV csv;
							try {
								
								rt = new ReaderTxt(new FileReader("file.txt"));
								ArrayList<MetricClass> list = rt.execute();
								csv = new CreateCSV("testSet.csv", list);
								csv.execute();

								boolean convertFlag = CSVtoARFF.convert("trainingSet", trainingFileText.getText());
								if (!convertFlag) {
									JOptionPane.showMessageDialog(null,
											"Please, select a CSV training set with ',' as separator.", "Attention",
											JOptionPane.INFORMATION_MESSAGE, new ImageIcon("icons//cartella.png"));
									shell.close();
								}

								else {
									BufferedReader reader = new BufferedReader(new FileReader("trainingSet.arff"));
									Instances training = new Instances(reader);
									reader.close();
									CSVtoARFF.convert("testSet", "testSet.csv");
									BufferedReader reader1 = new BufferedReader(new FileReader("testSet.arff"));

									Instances test = new Instances(reader1);
									reader1.close();
									model = new Model(classifierComboBox.getText(), training, test);
									model.buildAndEvaluate();
									model.generateFilePredictions();
									getPreditions("results.csv");
									deleteFiles();
									shell.close();

								}
							} catch (FileNotFoundException a) {
								// TODO Auto-generated catch block
								shell.close();
								a.printStackTrace();
							} catch (IOException e1) {
								// TODO Auto-generated catch block
								shell.close();
								e1.printStackTrace();
							}
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
								BufferedReader reader1 = new BufferedReader(new FileReader("testSet.arff"));

								Instances test = new Instances(reader1);
								reader1.close();

								boolean convertFlag = CSVtoARFF.convert("trainingSet", trainingFileText.getText());
								if((!trainingFileText.getText().isEmpty() || !trainingFileText.getText().equals("")) &&
										(!classifierComboBox.getText().isEmpty() || !classifierComboBox.getText().equals(""))) {
									if (!convertFlag) {
										JOptionPane.showMessageDialog(null,
												"Please, select a CSV training set with ',' as separator.", "Attention",
												JOptionPane.INFORMATION_MESSAGE, new ImageIcon("icons//cartella.png"));
										shell.close();
										return;
									}
									BufferedReader reader = new BufferedReader(new FileReader("trainingSet.arff"));
									Instances training = new Instances(reader);
									reader.close();
									model = new Model(classifierComboBox.getText(), training, test);
								}
								    model.setpTestSet(test);
									model.buildAndEvaluate();
									model.generateFilePredictions();
									getPreditions("results.csv");
									deleteFiles();
									shell.close();
								
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
	
	public void getPreditions (String path) {
		try {
			ReaderCSV rcsv = new ReaderCSV(new FileReader(path));
			ArrayList<MetricClass> mc = rcsv.execute();
			boolean bug = false;
			String info = "These classes have a bug: ";
			for(MetricClass m : mc) {
				if(m.getBug()) {
					info +=m.getName()+" ";
					bug = true;
				}
			}
			
			if(!bug) {
				info += "none";
			}
			JOptionPane.showMessageDialog(null,info, "Attention",
					JOptionPane.INFORMATION_MESSAGE, new ImageIcon("icons//cartella.png"));
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void listClass (File partenza) { 
		File[] list=partenza.listFiles(); 
		int MAX=list.length;
		for (int i = 0; i<MAX; i++){
			if (list[i].isDirectory()) 
				listClass( list[i] ); 
			else {
				String nome = list[i].toString();
				if(nome.substring(nome.lastIndexOf(".")).equals(".class")) {
					
					int binPosition = nome.indexOf("\\bin\\");
					namesClasses.add(nome.substring(binPosition+5));
				}
			}
		}
		
	}
	
	public void deleteFiles() {
		File f1 = new File("file.txt");
		File f2 = new File("trainingSet.arff");
		File f3 = new File("testSet.csv");
		File f4 = new File("testSet.arff");
		if(f1.exists()) f1.delete();
		if(f2.exists()) f2.delete();
		if(f3.exists()) f3.delete();
		if(f4.exists()) f4.delete();
		
	}
}
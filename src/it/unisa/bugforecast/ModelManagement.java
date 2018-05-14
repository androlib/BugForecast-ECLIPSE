package it.unisa.bugforecast;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import javax.swing.ImageIcon;
import javax.swing.JOptionPane;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.DirectoryDialog;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.ISelectionService;
import org.eclipse.ui.forms.widgets.Form;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.Section;
import org.eclipse.ui.part.ViewPart;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.IPath;
import org.eclipse.jface.viewers.IStructuredSelection;

import weka.core.Instances;

public class ModelManagement extends ViewPart {
	protected static final String CLOSED_OPTION = null;
	private Form form;
	private FormToolkit toolkit;
	private Button saveModelButton;
	private Button loadModelButton;
	private Text trainingFileText;
	private Combo classifierComboBox;
	private Label classifierLabel;
	private Text nameModelText;
	private Label nameModelLabel;
	private Text outputModelText;
	private Label outputModelLabel;
	private Text loadModelText;
	private Label loadModelLabel;
	private Model model;
	private String options;
	private Button buildModelButton;

	/**
	 * The constructor.
	 */
	public ModelManagement() {
		options = null;
	}

	/**
	 * This is a callback that will allow us to create the viewer and initialize it.
	 */
	public void createPartControl(Composite parent) {
		toolkit = new FormToolkit(parent.getDisplay());

		form = toolkit.createForm(parent);
		GridLayout layout = new GridLayout(4, false);
		form.getBody().setLayout(layout);

		createModelBuildingSection();
		
		createBuildModel();
		
		createSubmitButton();
		
		createModelSavingSection();
		
		createSaveModel();
		
		createModelLoadingSection();

		createLoadModel();
		
		
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


	public void createModelBuildingSection () {
		GridData gridData = new GridData();
		gridData.horizontalAlignment = GridData.FILL;
		gridData.horizontalSpan = 4;
		Section modelBuildingSection = toolkit.createSection(form.getBody(), Section.DESCRIPTION | Section.TITLE_BAR);
		modelBuildingSection.setText("Model Building");
		modelBuildingSection.setLayoutData(gridData);
	}
	
	
	public void createModelSavingSection() {
		GridData gridData = new GridData();
		gridData.horizontalAlignment = GridData.FILL;
		gridData.horizontalSpan = 4;
		Section modelBuildingSection = toolkit.createSection(form.getBody(), Section.DESCRIPTION | Section.TITLE_BAR);
		modelBuildingSection.setText("Model Saving");
		modelBuildingSection.setLayoutData(gridData);
	}
	
	public void createModelLoadingSection() {
		GridData gridData = new GridData();
		gridData.horizontalAlignment = GridData.FILL;
		gridData.horizontalSpan = 4;
		Section modelBuildingSection = toolkit.createSection(form.getBody(), Section.DESCRIPTION | Section.TITLE_BAR);
		modelBuildingSection.setText("Model Loading");
		modelBuildingSection.setLayoutData(gridData);
	}
	
	public void createSaveModel() {
		GridData gridData = new GridData();
		gridData.horizontalAlignment = GridData.FILL;
		gridData.horizontalSpan = 3;
		nameModelLabel= new Label(form.getBody(), SWT.BORDER);
		nameModelLabel.setText("Insert Name");
		
		nameModelText = new Text(form.getBody(), SWT.BORDER);
		nameModelText.setEditable(true);
		nameModelText.setLayoutData(gridData);
		outputModelLabel = new Label(form.getBody(), SWT.BORDER);
		outputModelLabel.setText("Output Folder");
		
		outputModelText = new Text(form.getBody(), SWT.BORDER);
		outputModelText.setLayoutData(gridData);
		outputModelText.setEditable(false);
		outputModelText.addMouseListener(new MouseListener() {
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
					outputModelText.setText(outputFile);
				}
			}

			@Override
			public void mouseUp(MouseEvent e) {
			}
		});
		
		GridData gridData3 = new GridData();
		gridData3.horizontalAlignment = GridData.FILL;
		gridData3.horizontalSpan = 4;
		saveModelButton = toolkit.createButton(form.getBody(), "Save Model", SWT.NULL);
		saveModelButton.setLayoutData(gridData3);
		saveModelButton.addMouseListener(new MouseListener() {
			
			@Override
			public void mouseUp(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void mouseDown(MouseEvent e) {
				// TODO Auto-generated method stub
				trainingFileText.setText("");
				classifierComboBox.setText("");
				if(model == null) {
					JOptionPane.showMessageDialog(null, "No model is created.", "Attention",
							JOptionPane.INFORMATION_MESSAGE, new ImageIcon("icons//cartella.png"));
					return;
				}
				if(nameModelText.getText().isEmpty() || outputModelText.getText().isEmpty()) {
					JOptionPane.showMessageDialog(null, "Please, insert a name and an output folder.", "Attention",
							JOptionPane.INFORMATION_MESSAGE, new ImageIcon("icons//cartella.png"));
				}
				
				else {
					model.saveModel(outputModelText.getText(), nameModelText.getText());
				}
			}
			
			@Override
			public void mouseDoubleClick(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}
		});

		
		
	}
	
	public void createLoadModel() {
		GridData gridData = new GridData();
		gridData.horizontalAlignment = GridData.FILL;
		gridData.horizontalSpan = 3;
		loadModelLabel= new Label(form.getBody(), SWT.BORDER);
		loadModelLabel.setText("Model");
		
		loadModelText = new Text(form.getBody(), SWT.BORDER);
		loadModelText.setEditable(false);
		loadModelText.setLayoutData(gridData);
		
		loadModelText.addMouseListener(new MouseListener() {
			@Override
			public void mouseDoubleClick(MouseEvent e) {
			}

			@Override
			public void mouseDown(MouseEvent e) {

				FileDialog fileDialog = new FileDialog(form.getShell(), SWT.MULTI);
				fileDialog.setFilterPath("");
				fileDialog.setFilterExtensions(new String[] { "*.model" });
				fileDialog.setFilterNames(new String[] { "Model File" });

				String dataFile = fileDialog.open();

				if (dataFile != null) {
					String[] selectedFiles = fileDialog.getFileNames();
					StringBuffer stringBuffer = new StringBuffer(fileDialog.getFilterPath() + "\\");
					for (int i = 0; i < selectedFiles.length; i++) {
						stringBuffer.append(selectedFiles[i]);
					}
					loadModelText.setText(stringBuffer.toString());
				}

			}

			@Override
			public void mouseUp(MouseEvent e) {
			}
		});
		GridData gridData3 = new GridData();
		gridData3.horizontalAlignment = GridData.FILL;
		gridData3.horizontalSpan = 4;
		loadModelButton = toolkit.createButton(form.getBody(), "Load Model", SWT.NULL);
		loadModelButton.setLayoutData(gridData3);
		loadModelButton.addMouseListener(new MouseListener() {
			
			@Override
			public void mouseUp(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void mouseDown(MouseEvent e) {
				// TODO Auto-generated method stub
				if(loadModelText.getText().isEmpty()) {
					JOptionPane.showMessageDialog(null, "Please, select a model.", "Attention",
							JOptionPane.INFORMATION_MESSAGE, new ImageIcon("icons//cartella.png"));
					return;
				}
				try {
					ObjectInputStream in = new ObjectInputStream(new FileInputStream(loadModelText.getText()));
					Object o = in.readObject();
					if(o instanceof Model) {
					model = (Model) o;
					ObjectOutputStream out = new ObjectOutputStream
							(new FileOutputStream("Model.model"));
					out.writeObject(model);
					out.close();
					}
					else {
						JOptionPane.showMessageDialog(null, "This file isn't a model.", "Attention",
								JOptionPane.INFORMATION_MESSAGE, new ImageIcon("icons//cartella.png"));
					}
					in.close();
					trainingFileText.setText("");
					classifierComboBox.setText("");
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} catch (ClassNotFoundException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
			
			@Override
			public void mouseDoubleClick(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}
		});
	}
	
	public void createBuildModel() {
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

				options = JOptionPane.showInputDialog("Insert properties", select);
			}

			@Override
			public void mouseUp(MouseEvent e) {
			}
		});
	}

	private void createSubmitButton() {
		GridData gridData = new GridData();
		gridData.horizontalAlignment = GridData.FILL;
		gridData.horizontalSpan = 4;
		buildModelButton = toolkit.createButton(form.getBody(), "Build Model", SWT.NULL);
		
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

						if (trainingFileText.getText().isEmpty() || classifierComboBox.getText().isEmpty()) {
							JOptionPane.showMessageDialog(null, "Some input data are missing.", "Attention",
									JOptionPane.INFORMATION_MESSAGE, new ImageIcon("icons//cartella.png"));
						} else {

							try {

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

									model = new Model(classifierComboBox.getText(), training, options);
									ObjectOutputStream out = new ObjectOutputStream
											(new FileOutputStream("Model.model"));
									out.writeObject(model);
									out.close();

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
	
}
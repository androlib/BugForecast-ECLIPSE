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
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.DirectoryDialog;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.forms.widgets.Form;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.part.ViewPart;

import weka.classifiers.evaluation.Evaluation;
import weka.core.Instances;

public class Application extends ViewPart {
	protected static final String CLOSED_OPTION = null;
	private Form form;
	private FormToolkit toolkit;
	private Text trainingFileText;
	private Label testFileLabel;
	private Text testFileText;
	private Text outputFolderText;
	private Combo classifierComboBox;

	/**
	 * The constructor.
	 */
	public Application() {
	}

	/**
	 * This is a callback that will allow us to create the viewer and initialize it.
	 */
	public void createPartControl(Composite parent) {
		toolkit = new FormToolkit(parent.getDisplay());

		form = toolkit.createForm(parent);
		GridLayout layout = new GridLayout(4, false);
		form.getBody().setLayout(layout);

		createStrategiesSection();

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

		Label outputFolderLabel = new Label(form.getBody(), SWT.BORDER);
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

		String items[] = { "Log", "NB", "RBF", "MLP", "C45", "DTtable", "Voting", "Bagging", "Boosting",
				"Random Forest", "CODEP", "ASCI" };

		Label classifierLabel = new Label(form.getBody(), SWT.BORDER);
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

	private void createSubmitButton() {
		GridData gridData = new GridData();
		gridData.horizontalAlignment = GridData.FILL;
		gridData.horizontalSpan = 4;
		Button buildModelButton = toolkit.createButton(form.getBody(), "Build Model", SWT.NULL);
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
							// Build model
							File fInput = new File(testFileText.getText());
							File fOutput = new File("file.txt");
							CreateTask ct = new CreateTask(fInput, fOutput);
							ct.startTask();
							ReaderTxt rt;
							CreateCSV csv;
							try {
								boolean convertFlag;
								rt = new ReaderTxt(new FileReader("file.txt"));
								ArrayList<MetricClass> list = rt.execute();
								csv = new CreateCSV("testSet.csv", list);
								csv.execute();

								convertFlag = CSVtoARFF.convert("trainingSet", trainingFileText.getText());
								if (!convertFlag) {
									JOptionPane.showMessageDialog(null,
											"Please, select a CSV training set with ',' as separator.", "Attention",
											JOptionPane.INFORMATION_MESSAGE, new ImageIcon("icons//cartella.png"));
								}

								else {
									BufferedReader reader = new BufferedReader(new FileReader("trainingSet.arff"));
									Instances training = new Instances(reader);

									CSVtoARFF.convert("testSet", "testSet.csv");
									BufferedReader reader1 = new BufferedReader(new FileReader("testSet.arff"));

									Instances test = new Instances(reader1);

									Model model = new Model(classifierComboBox.getText(), training, test);
									model.buildAndEvaluate();
									model.generateFilePredictions(outputFolderText.getText());

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
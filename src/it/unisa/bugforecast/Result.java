package it.unisa.bugforecast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;

import javax.swing.ImageIcon;
import javax.swing.JOptionPane;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.forms.widgets.Form;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.Section;
import org.eclipse.ui.part.ViewPart;

public class Result extends ViewPart implements IViewPart {

	protected static final String CLOSED_OPTION = null;
	public Form form;
	public FormToolkit toolkit;
	public Composite composite;
	public TabFolder tabFolder;
	public TabItem tab2;
	public Application view;
	public Table table2;
	public Table table;
	public String stringa;
	public Application a;
	public TableItem item;
	public TableItem item2;
	public TableItem item3;
	public TableItem item4;
	public TableItem item5;
	public TableItem item6;

	public Result() {

	}

	public void createPartControl(Composite parent) {
		toolkit = new FormToolkit(parent.getDisplay());
		form = toolkit.createForm(parent);
		GridLayout layout = new GridLayout(4, false);
		form.getBody().setLayout(layout);
		createResultForm();

	}

	public void createResultForm() {
		GridData gridData = new GridData();
		gridData.horizontalAlignment = GridData.FILL;
		gridData.horizontalSpan = 3;

		CreateTablePredictions();


	}

	public void CreateTablePredictions() {
		GridData gridData = new GridData();
		gridData.horizontalAlignment = GridData.FILL;
		gridData.horizontalSpan = 4;
		GridLayout layout = new GridLayout(3, false);

		Section bugPredictions = toolkit.createSection(form.getBody(), Section.DESCRIPTION | Section.TITLE_BAR);
		bugPredictions.setText("Bug predictions");
		bugPredictions.setLayoutData(gridData);
		Composite sectionMetrics = toolkit.createComposite(bugPredictions);
		sectionMetrics.setLayout(layout);

		getPreditions();
		Table table = toolkit.createTable(sectionMetrics, SWT.BORDER_SOLID);
		table.setLinesVisible(true);
		table.setHeaderVisible(true);
		table.setLayoutData(gridData);

		String[] titles = { "Class", "Bug","  " };
		for (int i = 0; i < (titles.length); i++) {
			TableColumn column = new TableColumn(table, SWT.NONE);
			column.setText(titles[i]);
		}

		if (mc != null) {
			for (MetricClass instance : mc) {
				TableItem item = new TableItem(table, SWT.NONE);
				String bug;
				if (instance.getBug()) {
					bug = "Yes";
				} else {
					bug = "No";
				}
				item.setText(0, instance.getName());
				item.setText(1, bug);
				
			}
			
			
		}
		for (int i = 0; i < titles.length; i++) {
			table.getColumn(i).pack();
		}

		bugPredictions.setClient(sectionMetrics);
		table.pack();
	}

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

	public void getPreditions() {
		try {
			File fResults = new File(path);
			if (fResults.exists()) {
				ReaderCSV rcsv = new ReaderCSV(new FileReader(path));
				mc = rcsv.execute();
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private String path = "results.csv";
	private ArrayList<MetricClass> mc;
}
package it.unisa.bugforecast;

import java.util.Arrays;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.ShellAdapter;
import org.eclipse.swt.events.ShellEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.List;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

public class MultiSelectionCombo extends Composite {

   Shell    shell               = null;
   List     list                = null;

   Text     txtCurrentSelection = null;

   String[] textItems           = null;
   int[]    currentSelection    = null;

   public MultiSelectionCombo(Composite parent, String[] items, int[] selection, int style) {
      super(parent, style);
      currentSelection = selection;
      textItems = items;
      init();
   }

   private void init() {
      GridLayout layout = new GridLayout();
      layout.marginBottom = 0;
      layout.marginTop = 0;
      layout.marginLeft = 0;
      layout.marginRight = 0;
      layout.marginWidth = 0;
      layout.marginHeight = 0;
      setLayout(new GridLayout());
      txtCurrentSelection = new Text(this, SWT.BORDER | SWT.READ_ONLY);
      txtCurrentSelection.setLayoutData(new GridData(GridData.FILL_BOTH));

      displayText();

      txtCurrentSelection.addMouseListener(new MouseAdapter() {

         @Override
         public void mouseDown(MouseEvent event) {
            super.mouseDown(event);
            initFloatShell();
         }

      });
   }

   private void initFloatShell() {
      Point p = txtCurrentSelection.getParent().toDisplay(txtCurrentSelection.getLocation());
      Point size = txtCurrentSelection.getSize();
      Rectangle shellRect = new Rectangle(p.x, p.y + size.y, size.x, 0);
      shell = new Shell(MultiSelectionCombo.this.getShell(), SWT.NO_TRIM);

      GridLayout gl = new GridLayout();
      gl.marginBottom = 2;
      gl.marginTop = 2;
      gl.marginRight = 2;
      gl.marginLeft = 2;
      gl.marginWidth = 0;
      gl.marginHeight = 0;
      shell.setLayout(gl);

      list = new List(shell, SWT.BORDER | SWT.MULTI | SWT.H_SCROLL | SWT.V_SCROLL);
      for (String value: textItems) {
         list.add(value);
      }

      list.setSelection(currentSelection);

      GridData gd = new GridData(GridData.FILL_BOTH);
      list.setLayoutData(gd);

      shell.setSize(shellRect.width, 100);
      shell.setLocation(shellRect.x, shellRect.y);

      list.addMouseListener(new MouseAdapter() {

         @Override
         public void mouseUp(MouseEvent event) {
            super.mouseUp(event);
            currentSelection = list.getSelectionIndices();
            if ((event.stateMask & SWT.CTRL) == 0) {
               shell.dispose();
               displayText();
            }
         }
      });

      shell.addShellListener(new ShellAdapter() {

         public void shellDeactivated(ShellEvent arg0) {
            if (shell != null && !shell.isDisposed()) {
               currentSelection = list.getSelectionIndices();
               displayText();
               shell.dispose();
            }
         }
      });
      shell.open();
   }

   private void displayText() {
      if (currentSelection != null && currentSelection.length > 0) {
         StringBuffer sb = new StringBuffer();
         for (int i = 0; i < currentSelection.length; i++) {
            if (i > 0)
               sb.append(", ");
            sb.append(textItems[currentSelection[i]]);
         }
         txtCurrentSelection.setText(sb.toString());
      }
      else {
         txtCurrentSelection.setText("");
      }
   }

   public int[] getSelections() {
      return this.currentSelection;
   }

   // Main method to showcase MultiSelectionCombo
   // (can be removed from productive code)
   public static void main(String[] args) {
      Display display = new Display();
      Shell shell = new Shell(display);
      shell.setLayout(new GridLayout());
      shell.setText("MultiSelectionCombo Demo");

      // Items and pre-selected items in combo box
      String[] items = new String[] { "Alpha", "Beta", "Gamma", "Delta", "Epsilon", "Zeta", "Eta", "Theta", "Iota", "Kappa" };
      int[] selection = new int[] { 0, 2 };

      // Create MultiSelectCombo box
      final MultiSelectionCombo combo = new MultiSelectionCombo(shell, items, selection, SWT.NONE);
      combo.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, true, false));
      ((GridData)combo.getLayoutData()).widthHint = 300;

      // Add button to print current selection on console
      Button button = new Button(shell, SWT.NONE);
      button.setText("What is selected?");
      button.addSelectionListener(new SelectionAdapter() {
         public void widgetSelected(SelectionEvent event) {
            System.out.println("Selected items: " + Arrays.toString(combo.getSelections()));
         }
      });

      shell.pack();
      shell.open();
      while (!shell.isDisposed()) {
         if (!display.readAndDispatch()) {
            display.sleep();
         }
      }
      display.dispose();
   }

}
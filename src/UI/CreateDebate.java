package UI;

import JSON_translation.*;

import javax.swing.JButton;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.SpinnerDateModel;
import javax.swing.text.MaskFormatter;
import javax.swing.BoxLayout;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import java.util.Date;

public class CreateDebate extends JPanel implements ActionListener {
	// Mods and Admin only
	public JLabel date1, date2, title1, description1;
	public JFormattedTextField startDate, endDate;
	public JSpinner startSpinner, endSpinner;
	public JTextField title, description;
	public JButton submit;
	public Date d1;
	public String start, end;
	public DebateQuery dq;
	public Frame frame;
	
	public CreateDebate(Frame frame) {
		
        //SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
       
		startSpinner = new JSpinner( new SpinnerDateModel() );
		JSpinner.DateEditor timeEditor = new JSpinner.DateEditor(startSpinner, "MM/dd/yyyy hh:mm a");
		startSpinner.setEditor(timeEditor);
		
		endSpinner = new JSpinner ( new SpinnerDateModel());
		JSpinner.DateEditor timeEditor2 = new JSpinner.DateEditor(endSpinner, "MM/dd/yyyy hh:mm a");
		endSpinner.setEditor(timeEditor2);
		
        /*startDate = new JFormattedTextField(createFormatter("##/##/#### ##:##"));
        startDate.setColumns(20);
        
        endDate = new JFormattedTextField(createFormatter("##/##/#### ##:##"));
        endDate.setColumns(20);
        */
		
		
		this.frame = frame;
		dq = new DebateQuery();
		this.setLayout(new BoxLayout(this, javax.swing.BoxLayout.PAGE_AXIS));
		date1 = new JLabel("Start Date: ");
		this.add(date1);
		
		//this.add(startDate);
		this.add(startSpinner);
		date2 = new JLabel("End Date: ");
		this.add(date2);
		//this.add(endDate);
		this.add(endSpinner);
		title1 = new JLabel("Title: ");
		this.add(title1);
		title = new JTextField();
		this.add(title);
		description1 = new JLabel("Description: ");
		this.add(description1);
		description = new JTextField();
		this.add(description);
		submit = new JButton("Submit Post");
		submit.setActionCommand("submit");
		submit.addActionListener(this);
		this.add(submit);
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		// TODO Auto-generated method stub
		System.out.println(arg0.getActionCommand());
		System.out.println(startDate.getText());
		System.out.println(endDate.getText());
		if (arg0.getActionCommand().equals("submit")) {
			//start = dq.convertDateToString(startDate.getText());
			if (dq.checkForDuplicateDebateTitle(title.getText()) == false) {
				JOptionPane.showMessageDialog(this, "There was a conflict with the title you have chosen. Please fix and resubmit.", "Error", JOptionPane.ERROR_MESSAGE);
			}
			else if (dq.checkForOverLappingDates((String) startSpinner.getValue(), (String) endSpinner.getValue()) == false) {
				JOptionPane.showMessageDialog(this, "There was a conflict with the time you have chosen. Please fix and resubmit.", "Error", JOptionPane.ERROR_MESSAGE);
			}
			
			else {
				dq.createNewDebate(title.getText(), description.getText(), (String) startSpinner.getValue(), (String) endSpinner.getValue());
				
				JOptionPane.showMessageDialog(this, "Debate was created.");
				title.setText("");
				description.setText("");
				startDate.setText("");
				endDate.setText("");
			}
		}
	}
	
    private MaskFormatter createFormatter(String s) {
        MaskFormatter formatter = null;
        try {
            formatter = new MaskFormatter(s);
        } catch (java.text.ParseException exc) {
            System.err.println("formatter is bad: " + exc.getMessage());
            System.exit(-1);
        }
        return formatter;
    }
	
	
}

package gui;

import model.Edge;
import model.Flow;
import model.FlowMatrix;
import model.Pair;

import javax.swing.*;
import java.awt.*;
import java.util.*;
import java.util.List;

/**
 * Created by vviital on 19/09/15.
 */
public class GuiLab4 {

    private JFrame mainframe;

    private JTextField number;

    private JPanel leftPanel;

    private JPanel rightPanel;

    private int width = 1200;

    private int height = 600;

    private int n;

    private java.util.List<Component> left;

    private java.util.List<Component> right;

    private Flow flows;

    private int ly;

    private int ry;

    public GuiLab4(){
        this.mainframe = new JFrame("MinCostMaxFlow");
        this.mainframe.setLayout(null);
        this.mainframe.setSize(width, height);
        this.mainframe.setVisible(true);
        this.mainframe.setResizable(false);

        this.leftPanel = new JPanel();
        this.leftPanel.setLayout(null);
        this.leftPanel.setBounds(0, 0, width / 2, height);

        this.rightPanel = new JPanel();
        this.rightPanel.setLayout(null);
        this.rightPanel.setBounds(width / 2, 0, width / 2, height);

        this.mainframe.add(leftPanel);
        this.mainframe.add(rightPanel);

        int x = 20, y = 20;
        addLabel(this.leftPanel, x, y, 100, 20, "Number: "); x += 105;

        this.number = new JTextField();
        this.number.setText("0");
        this.number.setBounds(x, y, 100, 20); x += 105;
        this.leftPanel.add(number);

        this.left = new ArrayList();
        this.right = new ArrayList();

        JButton okButton = new JButton("go!!!");
        okButton.setBounds(x, y, 100, 20);
        okButton.addActionListener((event) -> {
            try{
                this.n = Integer.parseInt(this.number.getText());
                removeComponent(leftPanel, left);
                removeComponent(rightPanel, right);
                rightPanel.repaint();
                this.addMatrix();
            } catch (Exception e){

            }
        });
        leftPanel.add(okButton);

        leftPanel.repaint();

    }

    private JLabel addLabel(JPanel panel, int x, int y, int w, int h, String text){
        JLabel label = new JLabel(text);
        label.setBounds(x, y, w, h);
        panel.add(label);
        return label;
    }

    private void addRemovableLabel(JPanel panel, int x, int y, int w, int h, String text, List<Component> mem){
        mem.add(addLabel(panel, x, y, w, h, text));
    }

    private void removeComponent(JPanel panel, List<Component> mem){
        for(Component x : mem){
            panel.remove(x);
        }
        mem.clear();
    }

    private void addRemovableTextField(int row, int col, JPanel panel, int x, int y, int w, int h, String text, List<Component> mem){
        JTextField field = new TextBox(text, x, y, w, h, row, col);
        field.setBounds(x, y, w, h);
        panel.add(field);
        mem.add(field);
    }

    private void addMatrix(){
        int x = 20, y = 50;
        addRemovableLabel(this.leftPanel, x, y, 200, 20, "Enter adjacency matrix", this.left); y += 30;
        for(int i = 0; i < n; ++i) {
            int temp = x;
            for (int j = 0; j < n; j++) {
                addRemovableTextField(i + 1, j + 1, this.leftPanel, x, y, 40, 20, "0", this.left);
                x += 50;
            }
            x = temp;
            y += 30;
        }

        JButton goButton = new JButton("Flow!!!");
        goButton.setBounds(x, y, 100, 20); y += 30;
        this.ly = y;
        goButton.addActionListener((event) -> {
            try {
                flow();
            } catch (Exception e) {

            }
        });
        this.left.add(goButton);
        this.leftPanel.add(goButton);
        this.leftPanel.repaint();
    }

    private void flow(){
        List<Edge> edges = buildGraph();

        this.flows = new Flow(edges, 2 + this.n + this.n, 0, n + n + 1);
        Pair f = this.flows.getFlow();
        List<Pair> answer = this.flows.recoverAnwer();

        int x = 20, y = ly;

        this.addRemovableLabel(this.leftPanel, x, y, 200, 20, "Total flow: " + Long.toString(f.first), left); y += 30;
        this.addRemovableLabel(this.leftPanel, x, y, 200, 20, "Total cost: " + Long.toString(f.second), left); y += 30;

        this.addRemovableLabel(this.leftPanel, x, y, 200, 20, "Answer: ", left); y += 30;

        for(Pair ans : answer) {
            this.addRemovableLabel(this.leftPanel, x, y, 200, 20, ans.first + " man do " + ans.second + " work", left);
            y += 30;
        };

        this.leftPanel.repaint();

        this.curstep = 0;
        this.makeRightPanel();
    }

    private List<Edge> buildGraph(){
        List<Edge> edges = new ArrayList();
        int from = 0, sink = n + n + 1;
        this.left.stream().filter(x -> x instanceof TextBox).forEach(x -> {
            TextBox box = (TextBox) x;
            int i = box.getRow();
            int j = box.getCol();
            int cost = Integer.parseInt(box.getText());
            edges.add(new Edge(i, j + n, 1, cost));
        });
        for(int i = 0; i < n; ++i)
            edges.add(new Edge(from, i + 1, 1, 0));
        for(int i = 0; i < n; ++i)
            edges.add(new Edge(i + n + 1, sink, 1, 0));
        return edges;
    }


    private int curstep = 0;

    private void makeRightPanel(){
        removeComponent(this.rightPanel, this.right);
        int x = 20, y = 20;

        addRemovableLabel(this.rightPanel, x, y, 200, 20, "Current step : " + Integer.toString(curstep + 1), this.right); y += 30;

        y = drawMatrix(y);

        JButton prevButton = new JButton("Previous step");
        prevButton.setBounds(x, y, 200, 20);
        prevButton.addActionListener((event) -> {
            if (curstep != 0) {
                --curstep;
                makeRightPanel();
            }
        });
        this.right.add(prevButton);
        this.rightPanel.add(prevButton);

        x += 250;

        JButton nextButton = new JButton("Next step");
        nextButton.setBounds(x, y, 200, 20);
        nextButton.addActionListener((event) -> {
            if (curstep != this.flows.getStepNumber() - 1){
                ++curstep;
                this.makeRightPanel();
            }
        });
        this.right.add(nextButton);
        this.rightPanel.add(nextButton);

        this.rightPanel.repaint();

    }

    private int drawMatrix(int cy){
        int x = 20, y = cy;
        FlowMatrix matrix = this.flows.getStep(curstep);
        addRemovableLabel(this.rightPanel, x, y, 200, 20, "Current flow: " + matrix.getFlow(), right); y += 20;
        addRemovableLabel(this.rightPanel, x, y, 200, 20, "Current cost: " + matrix.getCost(), right); y += 20;
        addRemovableLabel(this.rightPanel, x, y, 500, 50, "First vertex - source, next n vertexes - left part of bipartite graph\n", right); y += 20;
        addRemovableLabel(this.rightPanel, x, y, 500, 50, "next n vertextes - right part of bipartite graph, the last vertex - sink", right); y += 40;
        for(int i = 0; i < matrix.size(); ++i){
            int temp = x;
            for(int j = 0; j < matrix.size(); ++j){
                addRemovableLabel(this.rightPanel, x, y, 40, 20, Long.toString(matrix.getFlow(i, j)), right);
                x += 40;
            }
            x = temp;
            y += 25;
        }
        y += 5;

        return y;
    }




}

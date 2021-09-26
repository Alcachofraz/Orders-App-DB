package OrdersApp;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.WindowEvent;
import java.sql.*;
import java.util.function.BiConsumer;
import OrdersApp.OptionHandler.Option;

public class OrdersApp {
    private static boolean isadmin = false;
    private static String cc;
    private static JFrame jf;

    public static boolean isAdmin() {
        return isadmin;
    }

    public static String getCc() {
        return cc;
    }

    public static void enterUser() {
        login((s, i) -> verifyLogin(DAL.getConnection(), s, i));
    }

    public static void login(BiConsumer<String, Integer> action) {
        jf.getContentPane().removeAll();

        JPanel p1 = new JPanel(new GridLayout(2, 1));
        JTextField tf1 = new JTextField(35);
        tf1.setBorder(new TitledBorder("Username"));
        JPasswordField pf1 = new JPasswordField(35);
        pf1.setBorder(new TitledBorder("Password"));
        p1.add(tf1);
        p1.add(pf1);

        JButton b1 = new JButton("Continuar");
        b1.addActionListener((b) -> action.accept(tf1.getText(), String.valueOf(pf1.getPassword()).hashCode()));

        jf.getContentPane().add(p1, BorderLayout.NORTH);
        jf.getContentPane().add(b1, BorderLayout.CENTER);
        OptionHandler.addExitButton(jf);
        jf.pack();
    }

    public static void verifyLogin(Connection ligacao, String username, int password) {
        try {
            ResultSet rs = DAL.getUsername(username, password);
            if (!rs.first()) {
                OptionHandler.sendError("Os credenciais inseridos estão incorretos.");
            } else {
                isadmin = (username.compareTo("admin") == 0);
                cc = rs.getString("ClienteCc");
                menuInterface();
            }
        } catch (SQLException sqlex) {
            sqlex.printStackTrace();
        }
    }

    public static void exit() {
        try {
            DAL.getConnection().close();
            jf.dispatchEvent(new WindowEvent(jf, WindowEvent.WINDOW_CLOSING));
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public static void orderMenuInterface() {
        jf.getContentPane().removeAll();

        JLabel l1 = new JLabel("   ", SwingConstants.CENTER);

        // Input buttons:
        JPanel p1 = new JPanel(new GridLayout(4, 1, 5, 5));
        p1.setBorder(new TitledBorder("Escolha uma opção"));

        addOptionButton(p1, "Procurar Menu num Estabelecimento", Option.ORDER_MENU_BY_ESTABLISHMENT);
        addOptionButton(p1, "Procurar Menu por Preço", Option.ORDER_MENU_BY_PRICE);
        addOptionButton(p1, "Menus Mais Recomendados", Option.ORDER_MENU_BY_RECOMMENDATION);
        addOptionButton(p1, "Estabelecimentos com Maior Pontuação", Option.ORDER_MENU_BY_APPRAISAL);

        jf.getContentPane().add(l1, BorderLayout.NORTH);
        jf.getContentPane().add(p1, BorderLayout.CENTER);
        OptionHandler.addBackButton(jf);
        jf.pack();
    }

    private static void addOptionButton(JPanel jp, String buttonText, Option option) {
        JButton b = new JButton(buttonText);
        b.addActionListener(new OptionHandler(jf, option));
        jp.add(b);
    }

    public static void menuInterface() {
        jf.getContentPane().removeAll();

        JLabel l1 = new JLabel("   ", SwingConstants.CENTER);

        JButton b;

        // Input buttons:
        JPanel p1 = new JPanel(new GridLayout(4, 1, 5, 5));
        p1.setBorder(new TitledBorder("Escolha uma opção"));

        // Client Related Buttons:
        JPanel p2 = new JPanel(new GridLayout(1, 3, 5, 5));
        p2.setBorder(new TitledBorder("Cliente"));

        b = new JButton("Encomendar Menu");
        b.addActionListener((e) -> {
            if (isAdmin()) OptionHandler.sendError("Só os clientes é que podem fazer isso.");
            else orderMenuInterface();
        });
        p2.add(b);

        addOptionButton(p2, "Recomendar Menu", Option.RECOMMEND_MENU);
        addOptionButton(p2, "Avaliar Estabelecimento", Option.APPRAISE_ESTABLISHMENT);
        p1.add(p2);

        // Administrator Related Buttons:
        JPanel p3 = new JPanel(new GridLayout(4, 3, 5, 5));
        p3.setBorder(new TitledBorder("Administrador"));

        addOptionButton(p3, "Submeter Menu", Option.SUBMIT_MENU);
        addOptionButton(p3, "Criar Utilizador", Option.CREATE_USER);
        addOptionButton(p3, "Criar Fornecedor", Option.CREATE_SUPPLIER);
        addOptionButton(p3, "Criar Transportador", Option.CREATE_TRANSPORTER);
        addOptionButton(p3, "Criar Estabelecimento", Option.CREATE_ESTABLISHMENT);
        addOptionButton(p3, "Criar Menu", Option.CREATE_MENU);
        addOptionButton(p3, "Criar Alimento", Option.CREATE_CONFECTION);
        addOptionButton(p3, "Criar Produto", Option.CREATE_PRODUCT);
        addOptionButton(p3, "Comprar Produtos", Option.BUY_PRODUCTS);
        addOptionButton(p3, "Produtos Disponíveis", Option.AVAILABLE_PRODUCTS);
        p1.add(p3);

        // Statistics Related Buttons:
        JPanel p4 = new JPanel(new GridLayout(2, 3, 5, 5));
        p4.setBorder(new TitledBorder("Estatísticas"));

        addOptionButton(p4, "Menu Mais Encomendado", Option.MOST_ORDERED_MENU);
        addOptionButton(p4, "Estabelecimento Mais Ativo", Option.MOST_ACTIVE_ESTABLISHMENT);
        addOptionButton(p4, "Fornecedor Mais Ativo", Option.MOST_ACTIVE_PROVIDER);
        addOptionButton(p4, "Lucro dos Estabelecimentos", Option.ESTABLISHMENT_PROFIT);
        addOptionButton(p4, "Lucro dos Transportadores", Option.TRANSPORTER_PROFIT);

        // Empty cell:
        p4.add(new JLabel(""));

        p1.add(p4);

        // Change User Button:
        JPanel p5 = new JPanel(new GridLayout(2, 3, 5, 5));

        // Empty cell:
        p5.add(new JLabel(""));
        p5.add(new JLabel(""));
        p5.add(new JLabel(""));
        p5.add(new JLabel(""));
        p5.add(new JLabel(""));

        JButton b0 = new JButton("Mudar de Utilizador");
        b0.addActionListener(new OptionHandler(jf, Option.CHANGE_USER));
        p5.add(b0);

        p1.add(p5);

        // Add all components to container:
        jf.getContentPane().add(l1, BorderLayout.NORTH);
        jf.getContentPane().add(p1, BorderLayout.CENTER);
        OptionHandler.addExitButton(jf);
        jf.pack();
    }

    public static void main(String[] args) {

        DAL.connect();

        jf = new JFrame();
        jf.setTitle("Encomendas");
        jf.setVisible(true);
        jf.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        enterUser();
    }
}

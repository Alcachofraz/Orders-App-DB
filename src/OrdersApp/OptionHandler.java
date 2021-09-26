package OrdersApp;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

public class OptionHandler implements ActionListener {
    public enum Option {
        ORDER_MENU_BY_ESTABLISHMENT,
        ORDER_MENU_BY_PRICE,
        ORDER_MENU_BY_RECOMMENDATION,
        ORDER_MENU_BY_APPRAISAL,
        RECOMMEND_MENU,
        APPRAISE_ESTABLISHMENT,
        SUBMIT_MENU,
        CREATE_USER,
        CREATE_SUPPLIER,
        CREATE_TRANSPORTER,
        CREATE_ESTABLISHMENT,
        CREATE_MENU,
        CREATE_CONFECTION,
        CREATE_PRODUCT,
        AVAILABLE_PRODUCTS,
        BUY_PRODUCTS,
        MOST_ORDERED_MENU,
        MOST_ACTIVE_ESTABLISHMENT,
        MOST_ACTIVE_PROVIDER,
        ESTABLISHMENT_PROFIT,
        TRANSPORTER_PROFIT,
        CHANGE_USER
    }

    private final JFrame jf;
    private final Option option;
    private static JLabel errorLabel;

    public static void sendError(String error) {
        errorLabel.setText(error);
    }


    public OptionHandler(JFrame jf, Option option) {
        this.jf = jf;
        this.option = option;
    }

    public void actionPerformed(ActionEvent e) {
        switch (option) {
            case ORDER_MENU_BY_ESTABLISHMENT:
                if (OrdersApp.isAdmin()) {
                    sendError("Só os clientes é que podem fazer isso.");
                    return;
                }
                try {
                    chooseIdFromTable(getTable(DAL.getEstablishments(true), "Estabelecimento", "Cod"), "Continuar", (estabCod) -> { // s1 -> Establishment
                        try {
                            chooseIdFromTable(getTable(DAL.getMenus(estabCod, true), "Menu", "Número de Pessoas", "Preço", "Estabelecimento", "Id"), "Continuar", (menuId) -> { // s2 -> MenuId
                                askFor("", "Encomendar", (address) -> {
                                    try {
                                        DAL.orderMenu(menuId, address[0]);
                                    } catch (SQLException throwables) {
                                        throwables.printStackTrace();
                                        sendError(throwables.getMessage());
                                        return;
                                    }
                                    completed("O menu foi encomendado.");
                                }, "Morada");
                            });
                        } catch (SQLException throwables) {
                            throwables.printStackTrace();
                            sendError(throwables.getMessage());
                        }
                    });
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                    sendError(throwables.getMessage());
                }
                break;
            case ORDER_MENU_BY_PRICE:
                if (OrdersApp.isAdmin()) {
                    sendError("Só os clientes é que podem fazer isso.");
                    return;
                }
                askFor("Limites de Preço", "Confirmar", (prices) -> {
                    try {
                        chooseIdFromTable(getTable(DAL.getMenus(Float.parseFloat(prices[0]), Float.parseFloat(prices[1])), "Menu", "Número de Pessoas", "Preço", "Estabelecimento", "Id"), "Continuar", (menuId) -> {
                            askFor("", "Encomendar", (address) -> {
                                try {
                                    DAL.orderMenu(menuId, address[0]);
                                } catch (SQLException throwables) {
                                    throwables.printStackTrace();
                                    sendError(throwables.getMessage());
                                    return;
                                }
                                completed("O menu foi encomendado.");
                            }, "Morada");
                        });
                    } catch (SQLException throwables) {
                        throwables.printStackTrace();
                        sendError(throwables.getMessage());
                    }
                }, "Preço mínimo", "Preço máximo");
                break;
            case ORDER_MENU_BY_RECOMMENDATION:
                if (OrdersApp.isAdmin()) {
                    sendError("Só os clientes é que podem fazer isso.");
                    return;
                }
                try {
                    chooseIdFromTable(getTable(DAL.getRecommendedMenus(20), "Menu", "Número de Pessoas", "Preço", "Estabelecimento", "Id"), "Continuar", (menuId) -> { // s2 -> MenuId
                        askFor("", "Encomendar", (address) -> {
                            try {
                                DAL.orderMenu(menuId, address[0]);
                            } catch (SQLException throwables) {
                                throwables.printStackTrace();
                                sendError(throwables.getMessage());
                                return;
                            }
                            completed("O menu foi encomendado.");
                        }, "Morada");
                    });
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                    sendError(throwables.getMessage());
                }
                break;
            case ORDER_MENU_BY_APPRAISAL:
                if (OrdersApp.isAdmin()) {
                    sendError("Só os clientes é que podem fazer isso.");
                    return;
                }
                try {
                    chooseIdFromTable(getTable(DAL.getEstablishmentsWithMostAppraisal(), "Estabelecimento", "Pontuação Média", "Cod"), "Continuar", (estabCod) -> { // s1 -> Establishment
                        try {
                            chooseIdFromTable(getTable(DAL.getMenus(estabCod, true), "Menu", "Número de Pessoas", "Preço", "Estabelecimento", "Id"), "Continuar", (menuId) -> { // s2 -> MenuId
                                askFor("", "Encomendar", (address) -> {
                                    try {
                                        DAL.orderMenu(menuId, address[0]);
                                    } catch (SQLException throwables) {
                                        throwables.printStackTrace();
                                        sendError(throwables.getMessage());
                                        return;
                                    }
                                    completed("O menu foi encomendado.");
                                }, "Morada");
                            });
                        } catch (SQLException throwables) {
                            throwables.printStackTrace();
                            sendError(throwables.getMessage());
                        }
                    });
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                    sendError(throwables.getMessage());
                }
                break;
            case RECOMMEND_MENU:
                if (OrdersApp.isAdmin()) {
                    sendError("Só os clientes é que podem fazer isso.");
                    return;
                }
                try {
                    chooseIdFromTable(getTable(DAL.getClients(true), "Cliente", "Cc"), "Continuar", (cc) -> {
                        try {
                            chooseIdFromTable(getTable(DAL.getEstablishments(true), "Estabelecimento", "Cod"), "Continuar", (estabCod) -> {
                                try {
                                    chooseIdFromTable(getTable(DAL.getMenus(estabCod, true), "Menu", "Número de Pessoas", "Preço", "Estabelecimento", "Id"), "Continuar", (menuId) -> { // s2 -> MenuId
                                        try {
                                            DAL.addRecommendation(cc, menuId);
                                        } catch (SQLException throwables) {
                                            throwables.printStackTrace();
                                            sendError(throwables.getMessage());
                                            return;
                                        }
                                        completed("Recomendação enviada.");
                                    });
                                } catch (SQLException throwables) {
                                    throwables.printStackTrace();
                                    sendError(throwables.getMessage());
                                }
                            });
                        } catch (SQLException throwables) {
                            throwables.printStackTrace();
                            sendError(throwables.getMessage());
                        }
                    });
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                    sendError(throwables.getMessage());
                }
                break;
            case APPRAISE_ESTABLISHMENT:
                if (OrdersApp.isAdmin()) {
                    sendError("Só os clientes é que podem fazer isso.");
                    return;
                }
                try {
                    chooseNameIdFromTable(getTable(DAL.getEstablishments(true), "Estabelecimento", "Cod"), "Continuar", (estabCod, estabName) -> {
                        askFor("Pontuar " + estabName[0], "Continuar", (appraise) -> {
                            int points = Integer.parseInt(appraise[0]);
                            if (points < 1 || points > 5) sendError("Pontuação inválida.");
                            else {
                                askForLarge("Avaliação", "Confirmar Avaliação", (justif) -> {
                                    try {
                                        DAL.addAppraisal(justif[0], points, estabCod[0]);
                                    } catch (SQLException throwables) {
                                        throwables.printStackTrace();
                                        sendError(throwables.getMessage());
                                        return;
                                    }
                                    completed("Avaliação publicada.");
                                }, "Justificação");
                            }
                        }, "Pontuação (1 a 5)");
                    });
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                    sendError(throwables.getMessage());
                }
                break;
            case SUBMIT_MENU:
                if (!OrdersApp.isAdmin()) {
                    sendError("Só um admninistrador é que pode fazer isso.");
                    return;
                }
                try {
                    chooseIdFromTable(getTable(DAL.getOrdersNotSent(true), "Data de Encomenda", "Destino", "Menu", "Cliente", "Id"), "Continuar", (enId) -> {
                        try {
                            chooseIdFromTable(getTable(DAL.getTransporters(true), "Transportador", "Cod"), "Submeter", (transCod) -> {
                                try {
                                    DAL.submitMenu(enId, transCod);
                                } catch (SQLException throwables) {
                                    throwables.printStackTrace();
                                    sendError(throwables.getMessage());
                                    return;
                                }
                                completed("O menu foi submetido.");
                            });
                        } catch (SQLException throwables) {
                            throwables.printStackTrace();
                            sendError(throwables.getMessage());
                        }
                    });
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                    sendError(throwables.getMessage());
                } break;
            case CREATE_USER:
                if (!OrdersApp.isAdmin()) {
                    sendError("Só um admninistrador é que pode fazer isso.");
                    return;
                }
                askFor("Utilizador", "Confirmar", (user) -> {
                    askFor("Cliente", "Confirmar", (client) -> {
                        try {
                            DAL.createUser(user[0], user[1].hashCode(), client[0], Integer.parseInt(client[1]));
                        } catch (SQLException throwables) {
                            throwables.printStackTrace();
                            sendError(throwables.getMessage());
                            return;
                        }
                        completed("Utilizador criado com sucesso.");
                    }, "Nome do Cliente", "Cartão de Cidadão");
                }, "Nome do Utilizador", "Palavra-Passe");
                break;
            case CREATE_SUPPLIER:
                if (!OrdersApp.isAdmin()) {
                    sendError("Só um admninistrador é que pode fazer isso.");
                    return;
                }
                askFor("", "Criar", (suppName) -> {
                    try {
                        DAL.addSupplier(suppName[0]);
                    } catch (SQLException throwables) {
                        throwables.printStackTrace();
                        sendError(throwables.getMessage());
                        return;
                    }
                    completed("Fornecedor criado com sucesso.");
                }, "Nome do Fornecedor");
                break;
            case CREATE_TRANSPORTER:
                if (!OrdersApp.isAdmin()) {
                    sendError("Só um admninistrador é que pode fazer isso.");
                    return;
                }
                askFor("", "Continuar", (estabName) -> {
                    chooseFromDropDown("Tipo de Transporte", "Criar Transportador", (type) -> {
                        try {
                            DAL.addTransporter(estabName[0], type.substring(0, 3));
                        } catch (SQLException throwables) {
                            throwables.printStackTrace();
                            sendError(throwables.getMessage());
                            return;
                        }
                        completed("Transportador criado com sucesso.");
                    }, "Carro", "Mota");
                }, "Nome do Transportador");
                break;
            case CREATE_ESTABLISHMENT:
                if (!OrdersApp.isAdmin()) {
                    sendError("Só um admninistrador é que pode fazer isso.");
                    return;
                }
                askFor("", "Continuar", (estabName) -> {
                    chooseFromDropDown("Tipo de Serviço", "Criar Estabelecimento", (type) -> {
                        try {
                            DAL.addEstablishment(estabName[0], type);
                        } catch (SQLException throwables) {
                            throwables.printStackTrace();
                            sendError(throwables.getMessage());
                            return;
                        }
                        completed("Estabelecimento criado com sucesso.");
                    }, "Comida e Bebida", "Apenas Bebida");
                }, "Nome do Estabelecimento");
                break;
            case CREATE_MENU:
                if (!OrdersApp.isAdmin()) {
                    sendError("Só um admninistrador é que pode fazer isso.");
                    return;
                }
                try {
                    chooseIdFromTable(getTable(DAL.getEstablishments(true), "Estabelecimento", "Cod"), "Continuar", (estabCod) -> {
                        askFor("Criar Menu", "Continuar", (menuParams) -> {
                            try {
                                chooseNameIdFromTable(getTable(DAL.getConfectionables(estabCod, true), "Alimentos Confecionáveis", "Id"), "Continuar", (alimIds, alimNames) -> {
                                    addSuffix(alimNames, " (Quantidade)");
                                    askFor("Quantidades", "Criar Menu", (quant) -> {
                                        try {
                                            DAL.addMenu(estabCod, menuParams[0], Integer.parseInt(menuParams[1]), Float.parseFloat(menuParams[2]), alimIds, Arrays.stream(quant).mapToInt(Integer::parseInt).toArray());
                                        } catch (SQLException throwables) {
                                            throwables.printStackTrace();
                                            sendError(throwables.getMessage());
                                            return;
                                        }
                                        completed("Menu adicionado.");
                                    }, alimNames);
                                });
                            } catch (SQLException throwables) {
                                throwables.printStackTrace();
                                sendError(throwables.getMessage());
                            }
                        }, "Nome do Menu", "Número de Pessoas", "Preço do menu");
                    });
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                    sendError(throwables.getMessage());
                }
                break;
            case CREATE_CONFECTION:
                if (!OrdersApp.isAdmin()) {
                    sendError("Só um admninistrador é que pode fazer isso.");
                    return;
                }
                askFor("Criar Alimento", "Continuar", (alimName) -> {
                    chooseFromDropDown("Tipo de Alimento", "Continuar", (type) -> {
                        try {
                            chooseNameIdFromTable(getTable(DAL.getProducts(true), "Produto", "Cuu"), "Continuar", (prodCuus, prodNames) -> {
                                askFor("Quantidades", "Criar Alimento", (quant) -> {
                                    try {
                                        DAL.addConfection(alimName[0], type, prodCuus, Arrays.stream(quant).mapToInt(Integer::parseInt).toArray());
                                    } catch (SQLException throwables) {
                                        throwables.printStackTrace();
                                        sendError(throwables.getMessage());
                                        return;
                                    }
                                    completed("Alimento adicionado.");
                                }, prodNames);
                            });
                        } catch (SQLException throwables) {
                            throwables.printStackTrace();
                            sendError(throwables.getMessage());
                        }
                    }, "Comida", "Bebida");
                }, "Nome do Alimento");
                break;
            case CREATE_PRODUCT:
                if (!OrdersApp.isAdmin()) {
                    sendError("Só um admninistrador é que pode fazer isso.");
                    return;
                }
                askFor("Criar Produto", "Continuar", (prodParams) -> {
                    try {
                        DAL.addProduct(prodParams[0], Integer.parseInt(prodParams[1]));
                    } catch (SQLException throwables) {
                        throwables.printStackTrace();
                        sendError(throwables.getMessage());
                        return;
                    }
                    completed("Produto adicionado.");
                }, "Nome do Produto", "Código Único Universal");
                break;
            case AVAILABLE_PRODUCTS:
                if (!OrdersApp.isAdmin()) {
                    sendError("Só um admninistrador é que pode fazer isso.");
                    return;
                }
                try {
                    chooseIdFromTable(getTable(DAL.getEstablishments(true), "Estabelecimento", "Cod"), "Continuar", (estabCod) -> {
                        try {
                            showTable(getTable(DAL.getBoughtProducts(estabCod, true), "Produto"));
                        } catch (SQLException throwables) {
                            throwables.printStackTrace();
                            sendError(throwables.getMessage());
                        }
                    });
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                    sendError(throwables.getMessage());
                }
                break;
            case BUY_PRODUCTS:
                if (!OrdersApp.isAdmin()) {
                    sendError("Só um admninistrador é que pode fazer isso.");
                    return;
                }
                try {
                    chooseIdFromTable(getTable(DAL.getEstablishments(true), "Estabelecimento", "Cod"), "Continuar", (estabCod) -> {
                        try {
                            chooseIdFromTable(getTable(DAL.getSuppliers(true), "Fornecedor", "Cod"), "Continuar", (suppCod) -> {
                                try {
                                    chooseNameIdFromTable(getTable(DAL.getNotBoughtProducts(estabCod, true), "Produto", "Cuu"), "Continuar", (prodCuus, prodNames) -> {
                                        askFor("Preços", "Comprar Produtos", (prices) -> {
                                            try {
                                                DAL.addSupplies(suppCod, estabCod, prodCuus, Arrays.stream(prices).map(Float::valueOf).toArray(Float[]::new));
                                            } catch (SQLException throwables) {
                                                throwables.printStackTrace();
                                                sendError(throwables.getMessage());
                                                return;
                                            }
                                            completed("Compra efetuada com sucesso.");
                                        }, prodNames);
                                    });
                                } catch (SQLException throwables) {
                                    throwables.printStackTrace();
                                    sendError(throwables.getMessage());
                                }
                            });
                        } catch (SQLException throwables) {
                            throwables.printStackTrace();
                            sendError(throwables.getMessage());
                        }
                    });
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                    sendError(throwables.getMessage());
                }
                break;
            case MOST_ORDERED_MENU:
                askFor("Menu Mais Encomendado do Ano", "Confirmar", (year) -> {
                    try {
                        showTable(getTable(DAL.getMostOrderedMenus(Integer.parseInt(year[0]), 20), "Menu", "Encomendas"));
                    } catch (SQLException throwables) {
                        throwables.printStackTrace();
                        sendError(throwables.getMessage());
                    }
                }, "Ano");
                break;
            case MOST_ACTIVE_ESTABLISHMENT:
                try {
                    showTable(getTable(DAL.getEstablishmentsWithMostOrders(), "Estabelecimento", "Encomendas"));
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                    sendError(throwables.getMessage());
                }
                break;
            case MOST_ACTIVE_PROVIDER:
                if (!OrdersApp.isAdmin()) {
                    sendError("Só um admninistrador é que pode fazer isso.");
                    return;
                }
                try {
                    showTable(getTable(DAL.getMostActiveSuppliers(), "Fornecedor", "Contribuições"));
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                    sendError(throwables.getMessage());
                }
                break;
            case ESTABLISHMENT_PROFIT:
                if (!OrdersApp.isAdmin()) {
                    sendError("Só um admninistrador é que pode fazer isso.");
                    return;
                }
                try {
                    showTable(getTable(DAL.getEstablishmentsProfit(true), "Estabelecimento", "Lucro"));
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                    sendError(throwables.getMessage());
                }
                break;
            case TRANSPORTER_PROFIT:
                if (!OrdersApp.isAdmin()) {
                    sendError("Só um admninistrador é que pode fazer isso.");
                    return;
                }
                try {
                    showTable(getTable(DAL.getTransportersProfit(true), "Transportador", "Lucro"));
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                    sendError(throwables.getMessage());
                }
                break;
            case CHANGE_USER:
                OrdersApp.enterUser();
                break;
            default:
                break;
        }
    }

    public void addSuffix(String[] str, String suffix) {
        for (int i = 0; i < str.length; i++) {
            str[i] += suffix;
        }
    }

    public void askFor(String title, String buttonText, MultiConsumer<String> action, String...fields) {
        jf.getContentPane().removeAll();

        int count = fields.length;

        JPanel p1 = new JPanel(new GridLayout((count%2 == 0) ? count/2 : (count+1)/2, 2));
        p1.setBorder(new TitledBorder(title));

        JTextField[] tf = new JTextField[count];

        for (int i = 0; i < count; i++) {
            tf[i] = new JTextField(35);
            tf[i].setBorder(new TitledBorder(fields[i]));
            p1.add(tf[i]);
        }
        JButton b1 = new JButton(buttonText);
        b1.addActionListener((b) -> {
            String[] text = new String[count];
            for (int i = 0; i < count; i++) {
                text[i] = tf[i].getText();
                if (text[i].equals("")) {
                    sendError("Não deixes campos vazios.");
                    return;
                }
            }
            action.accept(text);
        });
        jf.getContentPane().add(p1, BorderLayout.NORTH);
        jf.getContentPane().add(b1, BorderLayout.CENTER);
        addBackButton(jf);
        jf.pack();
    }

    public void askForLarge(String title, String buttonText, MultiConsumer<String> action, String...fields) {
        jf.getContentPane().removeAll();

        int count = fields.length;

        JPanel p1 = new JPanel(new GridLayout(count, 1));
        p1.setBorder(new TitledBorder(title));

        JTextArea[] tf = new JTextArea[count];

        for (int i = 0; i < count; i++) {
            tf[i] = new JTextArea(35, 3);
            tf[i].setBorder(new TitledBorder(fields[i]));
            p1.add(tf[i]);
        }
        JButton b1 = new JButton(buttonText);
        b1.addActionListener((b) -> {
            String[] text = new String[count];
            for (int i = 0; i < count; i++) {
                text[i] = tf[i].getText();
                if (text[i].equals("")) {
                    sendError("Não deixes campos vazios.");
                    return;
                }
            }
            action.accept(text);
        });
        jf.getContentPane().add(p1, BorderLayout.NORTH);
        jf.getContentPane().add(b1, BorderLayout.CENTER);
        addBackButton(jf);
        jf.pack();
    }

    public void chooseFromDropDown(String title, String buttonText, Consumer<String> action, String...options) {
        jf.getContentPane().removeAll();

        JComboBox<String> cb1 = new JComboBox<>(options);
        cb1.setBorder(new TitledBorder(title));
        cb1.setSelectedIndex(1);

        JButton b1 = new JButton(buttonText);
        b1.addActionListener((b) -> action.accept((String)cb1.getSelectedItem()));
        jf.getContentPane().add(cb1, BorderLayout.NORTH);
        jf.getContentPane().add(b1, BorderLayout.CENTER);
        addBackButton(jf);
        jf.pack();
    }

    private void showTable(JTable table) {
        jf.getContentPane().removeAll();

        JScrollPane scrollPane;
        scrollPane = new JScrollPane(table);
        table.setFillsViewportHeight(true);
        table.setDefaultEditor(Object.class, null);
        table.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
        jf.getContentPane().add(BorderLayout.CENTER, scrollPane);
        addBackButton(jf);

        jf.pack();
    }

    private void chooseNameIdFromTable(JTable table, String buttonText, BiConsumer<int[], String[]> action) {
        jf.getContentPane().removeAll();

        JScrollPane scrollPane;
        scrollPane = new JScrollPane(table);
        table.setFillsViewportHeight(true);
        table.setDefaultEditor(Object.class, null);
        table.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);

        // Remove id column (from view):
        int colCount = table.getColumnCount();
        TableColumnModel m = table.getColumnModel();
        TableColumn col = m.getColumn(colCount-1);
        m.removeColumn(col);

        JButton b1 = new JButton(buttonText);
        b1.addActionListener((b) -> {
            int[] rows = table.getSelectedRows();
            if (rows.length == 0) {
                sendError("Seleciona pelo menos um item");
            }
            else {
                int[] ids = new int[rows.length];
                String[] names = new String[rows.length];
                for (int i = 0; i < rows.length; i++) {
                    ids[i] = Integer.parseInt((String) table.getModel().getValueAt(rows[i], colCount-1));
                    names[i] = (String) table.getModel().getValueAt(rows[i], 0);
                }
                action.accept(ids, names);
            }
        });

        jf.getContentPane().add(scrollPane, BorderLayout.NORTH);
        jf.getContentPane().add(b1, BorderLayout.CENTER);
        addBackButton(jf);
        jf.pack();
    }

    private void chooseNameFromTable(JTable table, String buttonText, Consumer<String> action) {
        jf.getContentPane().removeAll();

        JScrollPane scrollPane;
        scrollPane = new JScrollPane(table);
        table.setFillsViewportHeight(true);
        table.setDefaultEditor(Object.class, null);
        table.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);

        // Remove id column (from view):
        int colCount = table.getColumnCount();
        TableColumnModel m = table.getColumnModel();
        TableColumn col = m.getColumn(colCount-1);
        m.removeColumn(col);

        JButton b1 = new JButton(buttonText);
        b1.addActionListener((b) -> {
            int row = table.getSelectedRow();
            if (row == -1) {
                sendError("Escolhe uma opção:");
            }
            else action.accept((String) table.getModel().getValueAt(row, 0));
        });

        jf.getContentPane().add(scrollPane, BorderLayout.NORTH);
        jf.getContentPane().add(b1, BorderLayout.CENTER);
        addBackButton(jf);
        jf.pack();
    }

    private void chooseIdFromTable(JTable table, String buttonText, Consumer<Integer> action) {
        jf.getContentPane().removeAll();

        JScrollPane scrollPane;
        scrollPane = new JScrollPane(table);
        table.setFillsViewportHeight(true);
        table.setDefaultEditor(Object.class, null);
        table.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);

        // Remove id column (from view):
        int colCount = table.getColumnCount();
        TableColumnModel m = table.getColumnModel();
        TableColumn col = m.getColumn(colCount-1);
        m.removeColumn(col);

        JButton b1 = new JButton(buttonText);
        b1.addActionListener((b) -> {
            int row = table.getSelectedRow();
            if (row == -1) {
                sendError("Escolhe uma opção:");
            }
            else action.accept(Integer.parseInt((String) table.getModel().getValueAt(row, colCount-1)));
        });

        jf.getContentPane().add(scrollPane, BorderLayout.NORTH);
        jf.getContentPane().add(b1, BorderLayout.CENTER);
        addBackButton(jf);
        jf.pack();
    }

    public void completed(String text) {
        jf.getContentPane().removeAll();

        JLabel l1 = new JLabel(text, SwingConstants.CENTER);
        l1.setBorder(new EmptyBorder(10, 10, 10, 10));
        jf.getContentPane().add(l1, BorderLayout.CENTER);
        addBackButton(jf);

        jf.pack();
    }

    public JTable getTable(ResultSet rs, String...headers) throws SQLException {
        ResultSetMetaData meta = rs.getMetaData();
        int resCols = meta.getColumnCount();
        int colCount = headers.length;

        if (colCount > resCols) return null;

        rs.last();
        int rowCount = rs.getRow();
        rs.beforeFirst();

        String[][] rowData = new String[rowCount][colCount];

        // Iterate Result set
        for (int i = 0; i < rowCount; i++) {
            rs.next();
            for (int j = 0; j < colCount; j++) {
                rowData[i][j] = rs.getString(j + 1);
            }
        }
        return new JTable(rowData, headers);
    }

    public static void addBackButton(JFrame _jf) {
        JPanel p3 = new JPanel();
        errorLabel = new JLabel();
        errorLabel.setPreferredSize(new Dimension(500, 40));
        errorLabel.setBorder(new TitledBorder("Erros"));
        errorLabel.setText("");
        p3.add(errorLabel);
        JButton b0 = new JButton("Voltar");
        b0.addActionListener((e) -> OrdersApp.menuInterface());
        p3.add(b0);
        _jf.getContentPane().add(BorderLayout.SOUTH, p3);
    }

    public static void addExitButton(JFrame _jf) {
        JPanel p3 = new JPanel();
        errorLabel = new JLabel();
        errorLabel.setPreferredSize(new Dimension(500, 40));
        errorLabel.setBorder(new TitledBorder("Erros"));
        errorLabel.setText("");
        p3.add(errorLabel);
        JButton b0 = new JButton("Sair");
        b0.addActionListener((e) -> OrdersApp.exit());
        p3.add(b0);
        _jf.getContentPane().add(BorderLayout.SOUTH, p3);
    }
}

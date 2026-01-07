
package visao;

import dao.ConvenioDAO;
import dao.PacienteDAO;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import javax.swing.JOptionPane;

import modelo.Convenio;
import modelo.Paciente;
import servicos.ConvenioServicos;
import servicos.ServicosFactory;

public class GuiCadPaciente extends javax.swing.JInternalFrame {
    
    // ==== ADICIONE ESTE CONSTRUTOR ====
    public GuiCadPaciente() {
        try {
            System.out.println("=== CRIANDO GuiCadPaciente ===");
            
            // 1. Inicialize os componentes da interface
            initComponents();
            
            // 2. DEFINA UM TAMANHO EXPLÍCITO (CRÍTICO!)
            this.setSize(700, 550);
            this.setPreferredSize(new java.awt.Dimension(700, 550));
            
            // 3. Posicione manualmente
            this.setLocation(50, 50);
            
            // 4. Configure propriedades
            this.setClosable(true);
            this.setIconifiable(true);
            this.setMaximizable(true);
            this.setResizable(true);
            this.setTitle("CADASTRO DE PACIENTE");
            
            // 5. Chame o método para preencher o combo (com try-catch)
            try {
                preencherCombo();
            } catch (Exception e) {
                System.out.println("Erro ao preencher combo: " + e.getMessage());
                // Adicione itens manuais para teste
                jcConvenio.removeAllItems();
                jcConvenio.addItem("-Selecione-");
                jcConvenio.addItem("Convênio Teste 1");
                jcConvenio.addItem("Convênio Teste 2");
            }
            
            // 6. Aplique máscaras (se tiver o método)
            aplicarMascaras();
            
            System.out.println("GuiCadPaciente criado com sucesso! Tamanho: " + this.getSize());
            
        } catch (Exception e) {
            System.out.println("ERRO CRÍTICO no construtor: " + e.getMessage());
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, 
                "Erro ao criar janela de cadastro: " + e.getMessage(), 
                "Erro", 
                JOptionPane.ERROR_MESSAGE);
        }
    }
    
    // ==== ADICIONE ESTE MÉTODO PARA MÁSCARAS ====
    private void aplicarMascaras() {
        // Máscara para CPF
        jtCpf.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                String text = jtCpf.getText().replaceAll("[^0-9]", "");
                if (text.length() <= 11) {
                    if (text.length() > 9) {
                        text = text.substring(0, 9) + "-" + text.substring(9);
                    }
                    if (text.length() > 6) {
                        text = text.substring(0, 6) + "." + text.substring(6);
                    }
                    if (text.length() > 3) {
                        text = text.substring(0, 3) + "." + text.substring(3);
                    }
                    jtCpf.setText(text);
                }
            }
        });
        
        // Máscara para telefone
        jtTelefone.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                String text = jtTelefone.getText().replaceAll("[^0-9]", "");
                if (text.length() <= 11) {
                    if (text.length() > 6) {
                        text = "(" + text.substring(0, 2) + ") " + text.substring(2, 7) + "-" + text.substring(7);
                    } else if (text.length() > 2) {
                        text = "(" + text.substring(0, 2) + ") " + text.substring(2);
                    } else if (text.length() > 0) {
                        text = "(" + text;
                    }
                    jtTelefone.setText(text);
                }
            }
        });
        
        // Máscara para data
        jtDataNasc.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                String text = jtDataNasc.getText().replaceAll("[^0-9]", "");
                if (text.length() <= 8) {
                    if (text.length() > 4) {
                        text = text.substring(0, 2) + "/" + text.substring(2, 4) + "/" + text.substring(4);
                    } else if (text.length() > 2) {
                        text = text.substring(0, 2) + "/" + text.substring(2);
                    }
                    jtDataNasc.setText(text);
                }
            }
        });
    }
    
    // ... o resto do seu código continua aqui (métodos de validação, cadastrar, limpar, etc.)
    /**
     * Creates new form GuiCadPaciente
     */
  // Método para validar CPF
private boolean validarCPF(String cpf) {
    // Remove caracteres não numéricos
    cpf = cpf.replaceAll("[^0-9]", "");
    
    if (cpf.length() != 11) {
        return false;
    }
    
    // Verifica se todos os dígitos são iguais
    if (cpf.matches("(\\d)\\1{10}")) {
        return false;
    }
    
    try {
        int soma = 0;
        // Primeiro dígito verificador
        for (int i = 0; i < 9; i++) {
            soma += (cpf.charAt(i) - '0') * (10 - i);
        }
        int resto = soma % 11;
        int digito1 = (resto < 2) ? 0 : 11 - resto;
        
        if ((cpf.charAt(9) - '0') != digito1) {
            return false;
        }
        
        // Segundo dígito verificador
        soma = 0;
        for (int i = 0; i < 10; i++) {
            soma += (cpf.charAt(i) - '0') * (11 - i);
        }
        resto = soma % 11;
        int digito2 = (resto < 2) ? 0 : 11 - resto;
        
        return (cpf.charAt(10) - '0') == digito2;
    } catch (Exception e) {
        return false;
    }
}

// Método para validar telefone
private boolean validarTelefone(String telefone) {
    // Remove caracteres não numéricos
    String tel = telefone.replaceAll("[^0-9]", "");
    
    // Telefone deve ter 10 ou 11 dígitos (com DDD)
    return tel.length() == 10 || tel.length() == 11;
}

// Método para validar email
private boolean validarEmail(String email) {
    String regex = "^[A-Za-z0-9+_.-]+@(.+)$";
    return email.matches(regex);
}


    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLayeredPane1 = new javax.swing.JLayeredPane();
        jlNome = new javax.swing.JLabel();
        jlCpf = new javax.swing.JLabel();
        jlEndereco = new javax.swing.JLabel();
        jtNome = new javax.swing.JTextField();
        jtCpf = new javax.swing.JTextField();
        jtEndereco = new javax.swing.JTextField();
        jlEspecialidade = new javax.swing.JLabel();
        jlDataNasc = new javax.swing.JLabel();
        jtDataNasc = new javax.swing.JTextField();
        jtTelefone = new javax.swing.JTextField();
        jlTelefone = new javax.swing.JLabel();
        jlEmail1 = new javax.swing.JLabel();
        jtEmail1 = new javax.swing.JTextField();
        jlRG = new javax.swing.JLabel();
        jtRG = new javax.swing.JTextField();
        jcConvenio = new javax.swing.JComboBox<>();
        jLayeredPane2 = new javax.swing.JLayeredPane();
        jbLimpar = new javax.swing.JButton();
        jbCadastrar1 = new javax.swing.JButton();

        setClosable(true);
        setIconifiable(true);
        setTitle("CADASTRO PACIENTE");

        jLayeredPane1.setBackground(new java.awt.Color(204, 255, 255));
        jLayeredPane1.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        jLayeredPane1.setOpaque(true);

        jlNome.setText("Nome");
        jLayeredPane1.add(jlNome);
        jlNome.setBounds(40, 20, 60, 30);

        jlCpf.setText("CPF");
        jLayeredPane1.add(jlCpf);
        jlCpf.setBounds(40, 60, 90, 30);

        jlEndereco.setText("Endereço");
        jLayeredPane1.add(jlEndereco);
        jlEndereco.setBounds(40, 140, 60, 30);
        jLayeredPane1.add(jtNome);
        jtNome.setBounds(140, 20, 210, 30);
        jLayeredPane1.add(jtCpf);
        jtCpf.setBounds(140, 60, 110, 30);
        jLayeredPane1.add(jtEndereco);
        jtEndereco.setBounds(140, 140, 210, 30);

        jlEspecialidade.setText("Convênio");
        jLayeredPane1.add(jlEspecialidade);
        jlEspecialidade.setBounds(40, 300, 100, 30);

        jlDataNasc.setText("Data Nascimento");
        jLayeredPane1.add(jlDataNasc);
        jlDataNasc.setBounds(40, 260, 100, 30);

        jtDataNasc.setToolTipText("(dd/mm/aaaa)");
        jLayeredPane1.add(jtDataNasc);
        jtDataNasc.setBounds(140, 260, 210, 30);

        jtTelefone.setToolTipText("(xx) xxxx-xxxx");
        jLayeredPane1.add(jtTelefone);
        jtTelefone.setBounds(140, 180, 130, 30);

        jlTelefone.setText("Telefone");
        jLayeredPane1.add(jlTelefone);
        jlTelefone.setBounds(40, 180, 50, 30);

        jlEmail1.setText("E-mal");
        jLayeredPane1.add(jlEmail1);
        jlEmail1.setBounds(40, 220, 90, 30);
        jLayeredPane1.add(jtEmail1);
        jtEmail1.setBounds(140, 220, 210, 30);

        jlRG.setText("RG");
        jLayeredPane1.add(jlRG);
        jlRG.setBounds(40, 100, 90, 30);
        jLayeredPane1.add(jtRG);
        jtRG.setBounds(140, 100, 210, 30);

        jLayeredPane1.add(jcConvenio);
        jcConvenio.setBounds(140, 300, 150, 30);

        jLayeredPane2.setBackground(new java.awt.Color(255, 255, 255));
        jLayeredPane2.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        jLayeredPane2.setOpaque(true);

        jbLimpar.setText("limpar");
        jbLimpar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbLimparActionPerformed(evt);
            }
        });
        jLayeredPane2.add(jbLimpar);
        jbLimpar.setBounds(290, 20, 140, 40);

        jbCadastrar1.setText("cadastrar");
        jbCadastrar1.setMinimumSize(new java.awt.Dimension(78, 20));
        jbCadastrar1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbCadastrar1ActionPerformed(evt);
            }
        });
        jLayeredPane2.add(jbCadastrar1);
        jbCadastrar1.setBounds(80, 20, 140, 40);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLayeredPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 534, Short.MAX_VALUE)
                    .addComponent(jLayeredPane2))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLayeredPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 371, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLayeredPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 85, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents
private void cadastrar() {
    try {
        // VALIDAÇÃO DOS CAMPOS OBRIGATÓRIOS
        if (jtNome.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Nome é obrigatório!", "Erro", JOptionPane.ERROR_MESSAGE);
            jtNome.requestFocus();
            return;
        }
        
        if (jtCpf.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "CPF é obrigatório!", "Erro", JOptionPane.ERROR_MESSAGE);
            jtCpf.requestFocus();
            return;
        } else if (!validarCPF(jtCpf.getText())) {
            JOptionPane.showMessageDialog(this, "CPF inválido!", "Erro", JOptionPane.ERROR_MESSAGE);
            jtCpf.requestFocus();
            return;
        }
        
        if (jtDataNasc.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Data de nascimento é obrigatória!", "Erro", JOptionPane.ERROR_MESSAGE);
            jtDataNasc.requestFocus();
            return;
        }
        
        if (jtEndereco.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Endereço é obrigatório!", "Erro", JOptionPane.ERROR_MESSAGE);
            jtEndereco.requestFocus();
            return;
        }
        
        if (jtTelefone.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Telefone é obrigatório!", "Erro", JOptionPane.ERROR_MESSAGE);
            jtTelefone.requestFocus();
            return;
        } else if (!validarTelefone(jtTelefone.getText())) {
            JOptionPane.showMessageDialog(this, "Telefone inválido! Formato: (xx) xxxxx-xxxx", "Erro", JOptionPane.ERROR_MESSAGE);
            jtTelefone.requestFocus();
            return;
        }
        
        // VALIDAÇÃO DE EMAIL
        if (!jtEmail1.getText().trim().isEmpty() && !validarEmail(jtEmail1.getText())) {
            JOptionPane.showMessageDialog(this, "E-mail inválido!", "Erro", JOptionPane.ERROR_MESSAGE);
            jtEmail1.requestFocus();
            return;
        }
        
        // VALIDAÇÃO DE CONVÊNIO
        if (jcConvenio.getSelectedIndex() == 0) {
            JOptionPane.showMessageDialog(this, "Selecione um convênio!", "Erro", JOptionPane.ERROR_MESSAGE);
            jcConvenio.requestFocus();
            return;
        }

        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

        Paciente pac = new Paciente();

        // Atribuindo valores aos atributos do Paciente com base nos campos preenchidos pelo usuário na tela
        pac.setNome(jtNome.getText().trim());
        pac.setEndereco(jtEndereco.getText().trim());
        pac.setDataNascimento(sdf.parse(jtDataNasc.getText().trim()));
        pac.setTelefone(jtTelefone.getText().trim());
        pac.setCpf(jtCpf.getText().trim());
        pac.setRg(jtRG.getText().trim());
        pac.setEmail(jtEmail1.getText().trim());

        // Obtendo o nome do convênio selecionado pelo usuário
        String conv = jcConvenio.getSelectedItem().toString();

        // Criando objeto ConvenioDAO para buscar o convênio no banco de dados
        ConvenioDAO convDAO = new ConvenioDAO();

        // Buscando o convênio no banco de dados com base no nome selecionado pelo usuário
        Convenio convenio = convDAO.buscarConvenioFiltro(conv);

        // Atribuindo o ID do convênio ao paciente
        pac.setConvenio(convenio.getIdConvenio());

        // Criando objeto PacienteDAO para cadastrar o paciente no banco de dados
        PacienteDAO pacDAO = new PacienteDAO();
        pacDAO.cadastrarPaciente(pac);

        // Mensagem de sucesso
        JOptionPane.showMessageDialog(this, "Paciente cadastrado com sucesso!");

    } catch (Exception e) {
        JOptionPane.showMessageDialog(this,
                "ERRO! " + e.getMessage());
    } // fecha catch

}// fecha método

    //apaga valores dos campos
    private void limpar() {
     //apaga valores dos campos
    jtNome.setText("");
    jtEndereco.setText("");
    jtCpf.setText("");
    jtDataNasc.setText("");
    jtTelefone.setText("");
    jtEmail1.setText("");
    jtRG.setText("");
    jcConvenio.setSelectedIndex(0);
}// fecha método
    

    
    // metodo para preencher o combo box com os produtos cadastrados no banco de dados
    private void preencherCombo() {
        try {

            // Buscando objeto ProdutoServicos
            ConvenioServicos ps = ServicosFactory.getConvenioServicos();

            /*
             * Criando um ArrayList<ProdutoVO> vazio
             * para receber o ArrayList com os dados
             */
            ArrayList<Convenio> p = new ArrayList<>();

            // Recebendo o ArrayList cheio em produtos
            p = ps.buscarConvenio();

            // Adicionando os dados do ArrayList no JComboBox
            jcConvenio.addItem("-Selecione-");
            for (int i = 0; i < p.size(); i++) {

                // Adicionando o nome do convênio ao JComboBox
                jcConvenio.addItem(p.get(i).getNomeConvenio());

            } // fecha for

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                    "Erro! " + e.getMessage());
        } // fecha catch
    }// fecha classe

    private void jbLimparActionPerformed(java.awt.event.ActionEvent evt) {
        limpar();
    }

    private void jbCadastrar1ActionPerformed(java.awt.event.ActionEvent evt) {
        cadastrar();
        limpar();
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLayeredPane jLayeredPane1;
    private javax.swing.JLayeredPane jLayeredPane2;
    private javax.swing.JButton jbCadastrar1;
    private javax.swing.JButton jbLimpar;
    private javax.swing.JComboBox<String> jcConvenio;
    private javax.swing.JLabel jlCpf;
    private javax.swing.JLabel jlDataNasc;
    private javax.swing.JLabel jlEmail1;
    private javax.swing.JLabel jlEndereco;
    private javax.swing.JLabel jlEspecialidade;
    private javax.swing.JLabel jlNome;
    private javax.swing.JLabel jlRG;
    private javax.swing.JLabel jlTelefone;
    private javax.swing.JTextField jtCpf;
    private javax.swing.JTextField jtDataNasc;
    private javax.swing.JTextField jtEmail1;
    private javax.swing.JTextField jtEndereco;
    private javax.swing.JTextField jtNome;
    private javax.swing.JTextField jtRG;
    private javax.swing.JTextField jtTelefone;
    // End of variables declaration//GEN-END:variables
}

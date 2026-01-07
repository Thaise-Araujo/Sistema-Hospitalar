package dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import modelo.Paciente;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import persistencia.ConexaoBanco;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class PacienteDAOTest {
    
    @Mock
    private ConexaoBanco conexaoBanco;
    
    @Mock
    private Connection connection;
    
    @Mock
    private PreparedStatement preparedStatement;
    
    @Mock
    private ResultSet resultSet;
    
    // Como PacienteDAO cria ConexaoBanco no construtor, vamos usar uma abordagem diferente
    private PacienteDAO pacienteDAO;
    
    private Paciente paciente;
    
    @Before
    public void setUp() throws SQLException {
        // Inicializar mocks
        MockitoAnnotations.openMocks(this);
        
        // Criar DAO manualmente e setar o mock
        pacienteDAO = new PacienteDAO();
        
        // Usar reflexão para injetar o mock de conexaoBanco
        // Isso é necessário porque PacienteDAO cria ConexaoBanco no construtor
        try {
            java.lang.reflect.Field field = PacienteDAO.class.getDeclaredField("conexao");
            field.setAccessible(true);
            field.set(pacienteDAO, conexaoBanco);
        } catch (Exception e) {
            fail("Não foi possível injetar o mock: " + e.getMessage());
        }
        
        // Configurar comportamento do mock de conexão
        when(conexaoBanco.getConexao()).thenReturn(connection);
        
        // Criar paciente de teste
        paciente = new Paciente();
        paciente.setNome("João Silva");
        paciente.setEndereco("Rua A, 123");
        paciente.setDataNascimento(new java.util.Date()); // Data atual
        paciente.setTelefone("11999999999");
        paciente.setCpf("12345678901");
        paciente.setRg("987654321");
        paciente.setIdConvenio(1);
    }
    
    @Test
    public void testCadastrarPacienteSucesso() throws SQLException {
        System.out.println("testCadastrarPacienteSucesso");
        
        // Configurar mocks
        when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);
        doNothing().when(preparedStatement).setString(anyInt(), anyString());
        doNothing().when(preparedStatement).setInt(anyInt(), anyInt());
        
        // Executar
        pacienteDAO.cadastrarPaciente(paciente);
        
        // Verificar
        verify(connection).prepareStatement("insert into PACIENTE(NOME, ENDERECO, DATA_NASC, TELEFONE, CPF, RG, ID_CONVENIO_FK) values(?,?,?,?,?,?,?)");
        verify(preparedStatement).setString(1, "João Silva");
        verify(preparedStatement).setString(2, "Rua A, 123");
        verify(preparedStatement).setString(4, "11999999999");
        verify(preparedStatement).setString(5, "12345678901");
        verify(preparedStatement).setString(6, "987654321");
        verify(preparedStatement).setInt(7, 1);
        verify(preparedStatement).execute();
        verify(connection).close();
    }
    
    @Test(expected = SQLException.class)
    public void testCadastrarPacienteComErroSQL() throws SQLException {
        System.out.println("testCadastrarPacienteComErroSQL");
        
        // Configurar mock para lançar exceção
        when(connection.prepareStatement(anyString())).thenThrow(new SQLException("Erro no banco"));
        
        // Executar - deve lançar exceção
        pacienteDAO.cadastrarPaciente(paciente);
    }
    
    @Test
    public void testBuscarPacienteFiltroComResultados() throws SQLException {
        System.out.println("testBuscarPacienteFiltroComResultados");
        
        // Configurar mocks
        when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);
        when(preparedStatement.executeQuery()).thenReturn(resultSet);
        when(resultSet.next()).thenReturn(true, false); // Um resultado
        
        // Configurar ResultSet mock
        when(resultSet.getInt("ID_PACIENTE")).thenReturn(1);
        when(resultSet.getString("NOME")).thenReturn("João Silva");
        when(resultSet.getString("ENDERECO")).thenReturn("Rua A, 123");
        when(resultSet.getDate("DATA_NASC")).thenReturn(new Date(System.currentTimeMillis()));
        when(resultSet.getString("TELEFONE")).thenReturn("11999999999");
        when(resultSet.getString("CPF")).thenReturn("12345678901");
        when(resultSet.getString("RG")).thenReturn("987654321");
        when(resultSet.getInt("ID_CONVENIO_FK")).thenReturn(1);
        
        // Executar
        String query = "WHERE NOME LIKE '%João%'";
        ArrayList<Paciente> resultado = pacienteDAO.buscarPacienteFiltro(query);
        
        // Verificar
        assertNotNull("Resultado não deve ser nulo", resultado);
        assertEquals("Deve retornar 1 paciente", 1, resultado.size());
        
        Paciente pacienteRetornado = resultado.get(0);
        assertEquals("ID deve ser 1", 1, pacienteRetornado.getIdPaciente());
        assertEquals("Nome deve ser João Silva", "João Silva", pacienteRetornado.getNome());
        assertEquals("CPF deve ser 12345678901", "12345678901", pacienteRetornado.getCpf());
        
        verify(connection).prepareStatement("SELECT * FROM paciente WHERE NOME LIKE '%João%'");
        verify(preparedStatement).executeQuery();
        verify(resultSet, times(2)).next(); // next() chamado duas vezes (true, false)
        verify(connection).close();
    }
    
    @Test
    public void testBuscarPacienteFiltroSemResultados() throws SQLException {
        System.out.println("testBuscarPacienteFiltroSemResultados");
        
        // Configurar mocks
        when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);
        when(preparedStatement.executeQuery()).thenReturn(resultSet);
        when(resultSet.next()).thenReturn(false); // Nenhum resultado
        
        // Executar
        ArrayList<Paciente> resultado = pacienteDAO.buscarPacienteFiltro("WHERE 1=0");
        
        // Verificar
        assertNotNull("Resultado não deve ser nulo", resultado);
        assertTrue("Resultado deve estar vazio", resultado.isEmpty());
        verify(resultSet, times(1)).next();
        verify(connection).close();
    }
    
    @Test
    public void testBuscarTodosPacientes() throws SQLException {
        System.out.println("testBuscarTodosPacientes");
        
        // Configurar mocks
        when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);
        when(preparedStatement.executeQuery()).thenReturn(resultSet);
        when(resultSet.next()).thenReturn(true, true, false); // Dois resultados
        
        // Configurar ResultSet mock para dois pacientes
        when(resultSet.getInt("ID_PACIENTE"))
            .thenReturn(1, 2);
        when(resultSet.getString("NOME"))
            .thenReturn("João Silva", "Maria Santos");
        when(resultSet.getString("ENDERECO"))
            .thenReturn("Rua A, 123", "Rua B, 456");
        when(resultSet.getDate("DATA_NASC"))
            .thenReturn(new Date(System.currentTimeMillis()), new Date(System.currentTimeMillis()));
        when(resultSet.getString("TELEFONE"))
            .thenReturn("11999999999", "11888888888");
        when(resultSet.getString("CPF"))
            .thenReturn("12345678901", "98765432100");
        when(resultSet.getString("RG"))
            .thenReturn("987654321", "123456789");
        when(resultSet.getInt("ID_CONVENIO_FK"))
            .thenReturn(1, 2);
        
        // Executar
        ArrayList<Paciente> resultado = pacienteDAO.buscarPaciente();
        
        // Verificar
        assertNotNull("Resultado não deve ser nulo", resultado);
        assertEquals("Deve retornar 2 pacientes", 2, resultado.size());
        
        verify(connection).prepareStatement("SELECT * FROM PACIENTE");
        verify(preparedStatement).executeQuery();
        verify(resultSet, times(3)).next(); // next() chamado três vezes (true, true, false)
        verify(connection).close();
    }
    
    @Test(expected = SQLException.class)
    public void testBuscarPacienteComErro() throws SQLException {
        System.out.println("testBuscarPacienteComErro");
        
        // Configurar mock para lançar exceção
        when(connection.prepareStatement(anyString())).thenThrow(new SQLException("Erro na consulta"));
        
        // Executar - deve lançar exceção
        pacienteDAO.buscarPaciente();
    }
    
    @Test
    public void testBuscarPacienteFiltroQueryVazia() throws SQLException {
        System.out.println("testBuscarPacienteFiltroQueryVazia");
        
        // Configurar mocks
        when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);
        when(preparedStatement.executeQuery()).thenReturn(resultSet);
        when(resultSet.next()).thenReturn(false);
        
        // Executar com query vazia
        ArrayList<Paciente> resultado = pacienteDAO.buscarPacienteFiltro("");
        
        // Verificar que a query é montada corretamente
        verify(connection).prepareStatement("SELECT * FROM paciente ");
        assertNotNull("Resultado não deve ser nulo", resultado);
        verify(connection).close();
    }
    
    @Test
    public void testConstrutorPacienteDAO() {
        System.out.println("testConstrutorPacienteDAO");
        
        // Testar criação normal do DAO
        PacienteDAO dao = new PacienteDAO();
        assertNotNull("DAO não deve ser nulo", dao);
    }
    
    // Teste para verificar tratamento de exceções
    @Test
    public void testSQLExceptionNoCadastro() {
        System.out.println("testSQLExceptionNoCadastro");
        
        try {
            // Configurar para lançar exceção
            when(connection.prepareStatement(anyString())).thenThrow(new SQLException("Teste de erro"));
            
            pacienteDAO.cadastrarPaciente(paciente);
            fail("Deveria ter lançado SQLException");
            
        } catch (SQLException e) {
            // Esperado
            assertTrue("Mensagem de erro deve conter 'Erro ao inserir dados'", 
                      e.getMessage().contains("Erro ao inserir dados"));
        }
    }
}
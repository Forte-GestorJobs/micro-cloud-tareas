package nttdata.messalhi.forte.utils;

public class ResultadoConsultaAWS {
    private boolean success;
    private String message;

    public ResultadoConsultaAWS(boolean exito, String mensaje) {
        this.success = exito;
        this.message = mensaje;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}

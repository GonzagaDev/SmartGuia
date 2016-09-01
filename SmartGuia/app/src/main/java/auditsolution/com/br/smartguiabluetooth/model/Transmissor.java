package auditsolution.com.br.smartguiabluetooth.model;

/**
 * Created by Win10-Home on 10/08/2016.
 */
public class Transmissor {
    private Integer _id;
    private Integer serial;
    private String rua;
    private String cruzamento;
    private String complemento;
    private String devName;
    private String devAddress;


    public Transmissor() {

    }

    public Transmissor(Integer _id, Integer serial, String rua, String cruzamento, String complemento, String devName, String devAddress) {
        this._id = _id;
        this.serial = serial;
        this.rua = rua;
        this.cruzamento = cruzamento;
        this.complemento = complemento;
        this.devName = devName;
        this.devAddress = devAddress;
    }

    public Integer getSerial() {
        return serial;
    }

    public void setSerial(Integer serial) {
        this.serial = serial;
    }

    public Integer get_id() {
        return _id;
    }

    public void set_id(Integer _id) {
        this._id = _id;
    }

    public String getRua() {
        return rua;
    }

    public void setRua(String rua) {
        this.rua = rua;
    }

    public String getCruzamento() {
        return cruzamento;
    }

    public void setCruzamento(String cruzamento) {
        this.cruzamento = cruzamento;
    }

    public String getComplemento() {
        return complemento;
    }

    public void setComplemento(String complemento) {
        this.complemento = complemento;
    }

    public String getDevName() {
        return devName;
    }

    public void setDevName(String devName) {
        this.devName = devName;
    }

    public String getDevAddress() {
        return devAddress;
    }

    public void setDevAddress(String devAddress) {
        this.devAddress = devAddress;
    }
}
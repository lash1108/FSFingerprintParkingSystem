package com.fingerprint.parkingfpaaccessmanager.service.usuario;

import com.fingerprint.parkingfpaaccessmanager.dao.usuario.UsuarioDao;
import com.fingerprint.parkingfpaaccessmanager.model.entity.Tblusr;
import com.fingerprint.parkingfpaaccessmanager.model.pojos.consume.ConsumeJsonGeneric;
import com.fingerprint.parkingfpaaccessmanager.model.pojos.consume.ConsumeJsonLong;
import com.fingerprint.parkingfpaaccessmanager.model.pojos.response.ResponseJsonBoolean;
import com.fingerprint.parkingfpaaccessmanager.model.pojos.response.ResponseJsonGeneric;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
public class UsuarioServiceImp implements UsuarioService {

    final
    UsuarioDao usuarioDao;

    public UsuarioServiceImp(UsuarioDao usuarioDao) {
        this.usuarioDao = usuarioDao;
    }

    @Override
    public ResponseJsonBoolean existUsrByParam(String param, int type) {
        ResponseJsonBoolean responseJson = new ResponseJsonBoolean();
        boolean response = switch (type) {
            case 1 -> usuarioDao.existsTblusrByEmailusr(param);
            case 2 -> usuarioDao.existsTblusrByLoginusr(param);
            default -> throw new IllegalStateException("Unexpected value: " + type);
        };

        responseJson.setSuccess(response);
        return responseJson;
    }


    @Override
    public ResponseJsonGeneric findUsrByParamAndPassword(String param, String password, int type) {
        ResponseJsonGeneric responseJson = new ResponseJsonGeneric();
        Map<String, Object> response = new LinkedHashMap<>();
        Tblusr usr;
        System.out.println(type + " " + param);

        switch (type) {
            case 1:
            case 2:
                if (param == null || password == null || param.isEmpty() || password.isEmpty()) {
                    response = this.notFoundResponse();
                } else if (type == 1 && !usuarioDao.existsTblusrByEmailusr(param)) {
                    response = this.notFoundResponse();
                } else if (type == 2 && !usuarioDao.existsTblusrByLoginusr(param)) {
                    response = this.notFoundResponse();
                } else if (type == 1 && usuarioDao.existsTblusrByEmailusr(param)) {
                    usr = findUserByEmailAndPassword(param, password);
                    if (usr != null) {
                        response.put(String.valueOf(usr.getCveusr()), this.fillUsr(usr));
                    } else {
                        response = this.badCredentialsResponse();
                    }
                } else if (type == 2 && usuarioDao.existsTblusrByLoginusr(param)) {
                    usr = findUserByLoginAndPassword(param, password);
                    if (usr != null) {
                        response.put(String.valueOf(usr.getCveusr()), this.fillUsr(usr));
                    } else {
                        response = this.badCredentialsResponse();
                    }
                }
                break;

            case 3:
                if (param == null || param.isEmpty()) {
                    response = this.notFoundResponse();
                } else if (!usuarioDao.existsTblusrByTokenusr(param)) {
                    response = this.notFoundResponse();
                } else if (usuarioDao.existsTblusrByTokenusr(param)) {
                    usr = usuarioDao.findTblusrByTokenusr(param);
                    if (usr != null) {
                        response.put(String.valueOf(usr.getCveusr()), this.fillUsr(usr));
                    } else {
                        response = this.badCredentialsResponse();
                    }
                }
                break;

            default:
                response = this.notFoundResponse();
                break;
        }

        responseJson.setDatos(response);
        return responseJson;
    }


    @Override
    public ResponseJsonGeneric createOrUpdateUser(ConsumeJsonGeneric consume) {
        /*0 = generico | 1 = pensionado | 2 = admin*/
        ResponseJsonGeneric responseJson = new ResponseJsonGeneric();
        Map<String, Object> request = consume.getDatos();
        new Tblusr();
        Tblusr usr;
        long cveusr = 0;

        String type = request.containsKey("typeusr") ? (String) request.get("typeusr") : "0";
        //Si hay una clave actualizamos el usr
        if (request.containsKey("cveusr")) {
            Integer cveUsrInteger = (Integer) request.get("cveusr");
            cveusr = cveUsrInteger.longValue();
            if (!usuarioDao.existsTblusrByCveusr(cveusr)) {
                return null;
            }
            usr = this.reFillUsr(request, cveusr);
        } else {
            usr = this.fillUsr(request, type);
        }

        usuarioDao.createOrUpdateUsuario(usr);
        request.put("cveUsr", cveusr);
        responseJson.setDatos(request);
        return responseJson;
    }

    @Override
    public ResponseJsonGeneric findAllUsersByTypeusr(String typeusr) {
        ResponseJsonGeneric responseJson = new ResponseJsonGeneric();
        Map<String, Object> response = new LinkedHashMap<>();
        List<Tblusr> users = usuarioDao.findAllUsrByTypeusr(typeusr);

        // Poblar el mapa con los resultados de users
        for (Tblusr user : users) {
            Map<String, Object> userData = new LinkedHashMap<>();
            long cveusr = user.getCveusr();
            userData.put("nameusr", user.getNameusr());
            userData.put("loginusr", user.getLoginusr());
            userData.put("emailusr", user.getEmailusr());
            userData.put("passwordusr", user.getPasswordusr());
            userData.put("idcar", this.findAllCarIdByCveusr(cveusr));
            userData.put("tokenusr", user.getTokenusr());
            userData.put("typeusr", user.getTypeusr());

            response.put(String.valueOf(cveusr), userData);
        }

        responseJson.setDatos(response);
        return responseJson;
    }

    @Override
    public ResponseJsonGeneric findUserByCveusr(ConsumeJsonLong consume) {
        ResponseJsonGeneric responseJson = new ResponseJsonGeneric();
        Map<String, Object> response = new LinkedHashMap<>();

        long cveusr = consume.getId();
        if (cveusr == 0) {
            response.put("Mensaje", "Clave de usuario no proporcionada.");
            response.put("codigo", HttpStatus.BAD_REQUEST.value());
        } else if (!usuarioDao.existsTblusrByCveusr(cveusr)) {
            response = this.notFoundResponse();
        } else {
            Tblusr usr = usuarioDao.findTblusrByCveusr(cveusr);
            response.put("Mensaje", "Usuario encontrado.");
            response.put(String.valueOf(usr.getCveusr()), this.fillUsr(usr));
            response.put("codigo", HttpStatus.OK.value());
        }


        responseJson.setDatos(response);
        return responseJson;
    }


    @Override
    public ResponseJsonGeneric deleteUsrByCveusr(ConsumeJsonLong consume) {
        Map<String, Object> response = new LinkedHashMap<>();
        ResponseJsonGeneric responseJson = new ResponseJsonGeneric();
        long requestID = consume.getId();
        if (requestID == 0L) {
            response.put("Mensaje", "Clave de usuario no proporcionada.");
            response.put("codigo", HttpStatus.BAD_REQUEST.value());
        } else if (!usuarioDao.existsTblusrByCveusr(requestID)) {
            response.put("Mensaje", "Usuario no encontrado.");
            response.put("codigo", HttpStatus.NOT_FOUND.value());
        } else {
            usuarioDao.deleteIdCarFromUsrByCveusr(requestID);
            usuarioDao.deleteTblusrByCveusr(requestID);
            response.put("Mensaje", "Usuario eliminado exitosamente.");
            response.put("cveUsr", requestID);
            response.put("codigo", HttpStatus.OK.value());
        }
        responseJson.setDatos(response);
        return responseJson;
    }

    /*CreateUsr*/
    private Tblusr fillUsr(Map<String, Object> data, String typeUsr) {
        Tblusr usr = new Tblusr();
        usr.setEmailusr((String) data.get("email"));
        usr.setLoginusr((String) data.get("login"));
        //noinspection unchecked
        usr.setIdcar((List<String>) data.get("idcar"));
        usr.setNameusr((String) data.get("nameusr"));
        usr.setPasswordusr((String) data.get("password"));
        usr.setTokenusr((String) data.get("token"));
        usr.setTypeusr(typeUsr);
        return usr;
    }

    /*Read Usr*/
    private Tblusr findUserByEmailAndPassword(String email, String password) {
        if (usuarioDao.existsTblusrByEmailusr(email)) {

            return usuarioDao.findTblusrByEmailusrAndPasswordusr(email, password);
        }
        return null;
    }

    private Tblusr findUserByLoginAndPassword(String login, String password) {
        if (usuarioDao.existsTblusrByLoginusr(login)) {
            return usuarioDao.findTblusrByLoginusrAndPasswordusr(login, password);
        }
        return null;
    }

    private Map<String, Object> fillUsr(Tblusr usr) {
        Map<String, Object> data = new LinkedHashMap<>();
        data.put("nameusr", usr.getNameusr());
        data.put("loginusr", usr.getLoginusr());
        data.put("emailusr", usr.getEmailusr());
        data.put("passwordusr", usr.getPasswordusr());
        data.put("idcar", this.findAllCarIdByCveusr(usr.getCveusr()));
        data.put("tokenusr", usr.getTokenusr());
        data.put("typeusr", usr.getTypeusr());
        return data;
    }

    //get Placas By clave
    private List<String> findAllCarIdByCveusr(long cveusr) {
        List<Object[]> results = usuarioDao.findAllCarIdByCveusr(cveusr);
        List<String> idCarList = new ArrayList<>();

        for (Object[] result : results) {
            if (result.length > 1 && result[1] instanceof String) {
                idCarList.add((String) result[1]);
            }
        }
        return idCarList;
    }


    /*UpdateUsr*/
    private Tblusr reFillUsr(Map<String, Object> data, long cveUsr) {
        Tblusr usr = usuarioDao.findTblusrByCveusr(cveUsr);
        usr.setEmailusr((String) data.get("email"));
        usr.setLoginusr((String) data.get("login"));
        @SuppressWarnings("unchecked") List<String> idCar = (List<String>) data.get("idcar");
        usr.setIdcar(idCar);
        usr.setNameusr((String) data.get("nameusr"));
        usr.setPasswordusr((String) data.get("password"));
        usr.setTokenusr((String) data.get("token"));
        usr.setTypeusr((String) data.get("typeusr"));
        return usr;
    }

    private Map<String, Object> notFoundResponse() {
        Map<String, Object> notFoundResponse = new LinkedHashMap<>();

        notFoundResponse.put("Mensaje", "Usuario no encontrado.");
        notFoundResponse.put("clave", HttpStatus.NOT_FOUND.getReasonPhrase());
        notFoundResponse.put("codigo", HttpStatus.NOT_FOUND.value());

        return notFoundResponse;
    }

    private Map<String, Object> badCredentialsResponse() {
        Map<String, Object> badCredentialsResponse = new LinkedHashMap<>();

        badCredentialsResponse.put("Mensaje", "Credenciales de usuario no incorrectas.");
        badCredentialsResponse.put("clave", HttpStatus.UNAUTHORIZED.getReasonPhrase());
        badCredentialsResponse.put("codigo", HttpStatus.UNAUTHORIZED.value());

        return badCredentialsResponse;
    }

}

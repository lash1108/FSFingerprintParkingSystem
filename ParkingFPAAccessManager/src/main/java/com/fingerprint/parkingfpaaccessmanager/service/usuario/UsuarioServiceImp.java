package com.fingerprint.parkingfpaaccessmanager.service.usuario;

import com.fingerprint.parkingfpaaccessmanager.dao.usuario.UsuarioDao;
import com.fingerprint.parkingfpaaccessmanager.model.entity.Tblusr;
import com.fingerprint.parkingfpaaccessmanager.model.pojos.consume.ConsumeJsonGeneric;
import com.fingerprint.parkingfpaaccessmanager.model.pojos.consume.ConsumeJsonLong;
import com.fingerprint.parkingfpaaccessmanager.model.pojos.response.ResponseJsonBoolean;
import com.fingerprint.parkingfpaaccessmanager.model.pojos.response.ResponseJsonGeneric;
import com.fingerprint.parkingfpaaccessmanager.model.pojos.util.ResponseJsonHandler;
import org.springframework.stereotype.Service;

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
        ResponseJsonHandler response = new ResponseJsonHandler();
        Tblusr usr;

        if (param == null || param.isEmpty() || (type != 3 && (password == null || password.isEmpty()))) {
            return response.badRequestResponse("No key or param provided", null);
        }

        switch (type) {
            case 1:
                if (!usuarioDao.existsTblusrByEmailusr(param)) {
                    return response.notFoundResponse("User not found with email: " + param);
                }
                usr = findUserByEmailAndPassword(param, password);
                break;

            case 2:
                if (!usuarioDao.existsTblusrByLoginusr(param)) {
                    return response.notFoundResponse("User not found with login: " + param);
                }
                usr = findUserByLoginAndPassword(param, password);
                break;

            case 3:
                if (!usuarioDao.existsTblusrByTokenusr(param)) {
                    return response.notFoundResponse("User not found with token: " + param);
                }
                usr = usuarioDao.findTblusrByTokenusr(param);
                break;

            default:
                return response.notFoundResponse("Invalid search type");
        }

        if (usr != null) {
            return response.okResponse("User found", usr);
        } else {
            return response.notFoundResponse("User not found with provided credentials");
        }
    }

    @Override
    public ResponseJsonGeneric createOrUpdateUser(ConsumeJsonGeneric consume) {
        /*0 = admin | 1 = pensionado | 2 = generico*/
        ResponseJsonHandler response = new ResponseJsonHandler();

        Map<String, Object> request = consume.getDatos();
        new Tblusr();
        Tblusr usr;
        long cveusr;

        if (!request.containsKey("email") || !request.containsKey("login")
                || !request.containsKey("nameusr") || !request.containsKey("password")
                || !request.containsKey("token")
        ) {
            return response.badRequestResponse("Json Null or malformed", consume);
        }

        //verificamos si tiene un tipo de usuario de lo contrario sera generico 2
        String type = request.containsKey("typeusr") ? (String) request.get("typeusr") : "2";
        //Si hay una clave actualizamos el usr
        if (request.containsKey("cveusr")) {
            Integer cveUsrInteger = (Integer) request.get("cveusr");
            cveusr = cveUsrInteger.longValue();
            if (!usuarioDao.existsTblusrByCveusr(cveusr)) {
                return response.notFoundResponse("User not found");
            }
            usr = this.reFillUsr(request, cveusr);
        } else {
            if (usuarioDao.existsTblusrByTokenusr(request.get("token").toString())) {
                return response.badRequestResponse("The token is taken", request.get("token").toString());
            }

            if (usuarioDao.existsTblusrByLoginusr(request.get("login").toString())) {
                return response.badRequestResponse("The login is taken", request.get("login").toString());
            }

            if (usuarioDao.existsTblusrByEmailusr(request.get("email").toString())) {
                return response.badRequestResponse("The email is taken", request.get("email").toString());
            }

            usr = this.fillUsr(request, type);
        }

        cveusr = usuarioDao.createOrUpdateUsuario(usr).getCveusr();
        request.put("cveUsr", cveusr);
        return response.okResponse("User updated successfully", request);
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

    @Override
    public ResponseJsonGeneric findAllUsersByTypeusr(String typeusr) {
        ResponseJsonHandler response = new ResponseJsonHandler();

        return response.okResponse("All users", usuarioDao.findAllUsrByTypeusr(typeusr));
    }

    @Override
    public ResponseJsonGeneric findUserByCveusr(ConsumeJsonLong consume) {
        ResponseJsonHandler response = new ResponseJsonHandler();

        long cveusr = consume.getId();
        if (cveusr <= 0) {
            return response.badRequestResponse("No key was provided", null);
        }
        if (!usuarioDao.existsTblusrByCveusr(cveusr)) {
            return response.notFoundResponse("User not found");
        }

        return response.okResponse("User Found successfully", usuarioDao.findTblusrByCveusr(cveusr));
    }


    @Override
    public ResponseJsonGeneric deleteUsrByCveusr(ConsumeJsonLong consume) {
        ResponseJsonHandler response = new ResponseJsonHandler();
        long requestID = consume.getId();
        if (requestID <= 0L) {
            return response.badRequestResponse("No key was provided", null);
        }
        if (!usuarioDao.existsTblusrByCveusr(requestID)) {
            return response.notFoundResponse("User not found");
        }

        usuarioDao.deleteIdCarFromUsrByCveusr(requestID);
        usuarioDao.deleteTblusrByCveusr(requestID);

        return response.okResponse("User deleted successfully", consume.getId());
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

}

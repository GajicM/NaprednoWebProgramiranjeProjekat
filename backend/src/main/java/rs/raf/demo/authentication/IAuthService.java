package rs.raf.demo.authentication;


import rs.raf.demo.model.User;

public interface IAuthService {
    public String generateJWT(User user);

    public boolean isAuthorized(String token);
}

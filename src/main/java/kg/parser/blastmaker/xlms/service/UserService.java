//package kg.parser.blastmaker.xlms.service;
//
//import kg.parser.blastmaker.xlms.objects.User;
//import kg.parser.blastmaker.xlms.respositiry.UserRepository;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//
//import java.util.List;
//
//@Service
//public class UserService {
//
//    @Autowired
//    private UserRepository userRepository;
//
//    public User save(User user){
//        return userRepository.saveAndFlush(user);
//    }
//
//    public List<User> saveAll(List<User> users){
//        return userRepository.saveAllAndFlush(users);
//    }
//
//    public List<User> findAll(){
//        return userRepository.findAll();
//    }
//
//    public User findById(long id){
//        return userRepository.findById(id).get();
//    }
//
//    public User findByUserName(String name){
//        return userRepository.findbyName(name);
//    }
////
////    public void deletePlaylistByUser(Playlist playlist, User user){
////        user.getPlaylists().remove(playlist);
////        userRepository.saveAndFlush(user);
////    }
////
////    public void deleteMusicbyUser(Music music, User user){
////        user.getMusics().remove(music);
////        userRepository.saveAndFlush(user);
////    }
////
////    public List<User> findAdminRole(){
////        Specificator<User> spec = new Specificator<>(SearchCriteria.builder().key("role").operation(":").value(Role.ADMIN).build());
////        return userRepository.findAll(Specification.where(spec));
////    }
//
////    public User findByUserName(String name){
////        return userRepository.findByUsername(name);
////    }
//
//}

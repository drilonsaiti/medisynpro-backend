package com.example.medisyncpro.service.impl;



import com.example.medisyncpro.config.JwtService;
import com.example.medisyncpro.model.*;
import com.example.medisyncpro.model.dto.*;
import com.example.medisyncpro.model.enums.Role;
import com.example.medisyncpro.model.excp.*;
import com.example.medisyncpro.model.mapper.UserUsernameMapper;
import com.example.medisyncpro.repository.*;
import com.example.medisyncpro.service.*;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.GeneralSecurityException;
import java.util.*;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    private final UserUsernameMapper mapper;

    private final JwtService jwtService;
    private final DoctorService doctorService;
    private final ClinicService clinicServices;
    private final PatientService patientService;
    private final ReceptionistService receptionistService;
    private final ClinicRepository clinicRepository;
    private final PatientRepository patientRepository;
    private final DoctorRepository doctorRepository;
    private final ReceptionistRepository receptionistRepository;
    private final SpecializationRepository specializationRepository;
   // private JavaMailSender mailSender;



    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return userRepository.findByEmail(email).orElseThrow(EmailDoesntExistsException::new);
    }



    @Override
    public List<UserUsernameDto> findAll() {
        return this.userRepository.findAll()
                .stream()
                .map(mapper).collect(Collectors.toList());
    }


    private Role addUserBasedOnType(User user, String userType) throws Exception {
        switch (userType){
            case "clinic":
                clinicServices.save(new Clinic(user.getEmail(),user.getFullName(),""));
                return Role.ROLE_OWNER;
            case "doctor":
                doctorService.save(new CreateDoctorDto(user.getEmail(),user.getFullName()));
                return Role.ROLE_DOCTOR;
            case "patient":
                patientService.save(new CreatePatientDto(user.getEmail(),user.getFullName()));
                return Role.ROLE_PATIENT;
            case "receptionist":
                receptionistService.save(new CreateReceptionistDto(user.getEmail(),user.getFullName()));
                return Role.ROLE_RECEPTIONIST;
            default:
                throw new Exception("This type of user doesn't exists");
        }
    }



    @Override
    public User register(String fullName, String email, String password, String repeatPassword,String userType) throws Exception {
        if (email == null || email.isEmpty() || password == null || password.isEmpty())
            throw new InvalidEmailOrPasswordException();
        if (!password.equals(repeatPassword))
            throw new PasswordsDoNotMatchException();
        if (this.userRepository.findByEmail(email).isPresent())
            throw new EmailAlreadyExistsException(email);
        if (this.userRepository.findByEmail(email).isPresent())
            throw new EmailAlreadyExistsException(email);
        User user = new User(fullName,email, passwordEncoder.encode(password));

        Role role = addUserBasedOnType(user,userType);
        user.setRole(role);
        return userRepository.save(user);
    }

    @Override
    public UserUsernameDto findUser(String token) {
        String email = jwtService.extractUsername(token);
        return this.userRepository.findByEmail(email)
                .map(mapper).get();
    }

    @Override
    public UserProfileDto findUserProfile(String email) {
        User user = this.userRepository.findByEmail(email).orElseThrow(() -> new UserNotFoundException(email));


        if(user.getRole() == Role.ROLE_DOCTOR){
            Doctor d = this.doctorRepository.findByEmail(user.getEmail()).orElse(null);
            return new UserProfileDto(user.getEmail(), user.getImageUrl(), user.getFullName(),d.getEducation(),d.getSpecialization() != null ? d.getSpecialization().getSpecializationId() : null);
        }

        if(user.getRole() == Role.ROLE_PATIENT){
            Patient p = this.patientRepository.findByEmail(user.getEmail());
            return new UserProfileDto(user.getEmail(),user.getImageUrl(),user.getFullName(),p.getAddress(),p.getGender(),p.getContactNumber(),p.getBirthDay());
        }

        if(user.getRole() == Role.ROLE_OWNER){
            Clinic c = this.clinicRepository.findByEmailAddress(user.getEmail());
            return new UserProfileDto(user.getEmail(), user.getImageUrl(), c.getClinicName());
        }

        if(user.getRole() == Role.ROLE_RECEPTIONIST){
            return new UserProfileDto(user.getEmail(), user.getImageUrl(), user.getFullName());
        }
        return null;
    }


    @Override
    public void changePassword(ChangePasswordDto change) {
        User u = this.userRepository.findByEmail(jwtService.extractUsername(change.getToken())).orElseThrow(() -> new UsernameNotFoundException(jwtService.extractUsername(change.getToken())));
        if (passwordEncoder.matches(change.getOldPassword(), u.getPassword())) {
            if (change.getNewPassword().equals(change.getRepeatedPassword())) {
                u.setPassword(passwordEncoder.encode(change.getNewPassword()));
            } else {
                throw new PasswordsDoNotMatchException();
            }
        } else {
            throw new PasswordsDoNotMatchException();

        }
        this.userRepository.save(u);
    }

    @Override
    public void changeFullName(ChangeFullNameDto change) {
        User u = this.userRepository.findByEmail(jwtService.extractUsername(change.getToken())).orElseThrow(() -> new UsernameNotFoundException(jwtService.extractUsername(change.getToken())));
        if (passwordEncoder.matches(change.getPassword(), u.getPassword())) {

            if(u.getRole() == Role.ROLE_DOCTOR){
                Doctor d = this.doctorRepository.findByEmail(u.getEmail()).orElse(null);
                u.setFullName(change.getFullName());
                d.setDoctorName(change.getFullName());
                d.setEducation(change.getEducation());
                Specializations s = this.specializationRepository.findById(change.getSpecializations()).orElse(null);
                d.setSpecialization(s);
                doctorRepository.save(d);
            }else if(u.getRole() == Role.ROLE_PATIENT){
                Patient p = this.patientRepository.findByEmail(u.getEmail());
                u.setFullName(change.getFullName());
                p.setPatientName(change.getFullName());
                p.setGender(change.getGender());
                p.setAddress(change.getAddress());
                p.setContactNumber(change.getContactNumber());
                p.setBirthDay(change.getBirthDay());
                this.patientRepository.save(p);
            }else if(u.getRole() == Role.ROLE_RECEPTIONIST){
                Receptionist r = this.receptionistRepository.findByEmailAddress(u.getEmail()).orElse(null);
                r.setReceptionistName(change.getFullName());
                u.setFullName(change.getFullName());
                this.receptionistRepository.save(r);
            }else{
                Clinic c = clinicRepository.findByEmailAddress(u.getEmail());
                c.setClinicName(change.getFullName());
                c.setAddress(change.getAddress());
                u.setFullName(change.getFullName());
                this.clinicRepository.save(c);
            }
        } else {
            throw new PasswordsDoNotMatchException();

        }
        this.userRepository.save(u);
    }
    @Override
    public void changeAvatar(ChangeAvatarDto change) {
        User u = this.userRepository.findByEmail(jwtService.extractUsername(change.getToken())).orElseThrow(() -> new UsernameNotFoundException(jwtService.extractUsername(change.getToken())));

        switch (u.getRole()){
            case ROLE_OWNER ->{
                Clinic c = this.clinicRepository.findByEmailAddress(u.getEmail());
                c.setImageUrl(change.getAvatar());
                this.clinicRepository.save(c);
            }
            case ROLE_DOCTOR -> {
                Doctor d = this.doctorRepository.findByEmail(u.getEmail()).orElse(null);
                d.setImageUrl(change.getAvatar());
                this.doctorRepository.save(d);
            }
            case ROLE_PATIENT -> {
                Patient p = this.patientRepository.findByEmail(u.getEmail());
                p.setImageUrl(change.getAvatar());
                this.patientRepository.save(p);
            }
            case ROLE_RECEPTIONIST -> {
                Receptionist r = this.receptionistRepository.findByEmailAddress(u.getEmail()).orElse(null);
                r.setImageUrl(change.getAvatar());
                this.receptionistRepository.save(r);
            }

        }
        u.setImageUrl(change.getAvatar());
        this.userRepository.save(u);
    }

    @Override
    public void forgetPassword(ForgotPassword dto) {
        User u = null;
        if (dto.getToken() != "" && dto.getToken() != null) {
            u = userRepository.findByEmail(jwtService.extractUsername(dto.getToken()))
                    .orElseThrow(() -> new UsernameNotFoundException(jwtService.extractUsername(dto.getToken())));

            if (!u.getEmail().equals(dto.getEmail())) {
                throw new EmailDoesntExistsException();
            }

        } else {
            u = userRepository.findByEmail(dto.getEmail())
                    .orElseThrow(EmailDoesntExistsException::new);
        }
        if (!dto.getNewPassword().equals(dto.getRepeatedPassword())) {
            throw new PasswordsDoNotMatchException();
        }
        /*if (!u.getVerifyCode().equals(dto.getVerifyCode())) {
            throw new VerificationCodeDoNotMatchException();
        }*/


        u.setPassword(passwordEncoder.encode(dto.getNewPassword()));
        this.userRepository.save(u);
    }

    @Override
    public void changeEmail(ChangeEmailDto change) {
        User u = this.userRepository.findByEmail(jwtService.extractUsername(change.getToken())).orElseThrow(() -> new UsernameNotFoundException(jwtService.extractUsername(change.getToken())));
        if (passwordEncoder.matches(change.getPassword(), u.getPassword())) {
            if (change.getVerifyCode().equals(u.getVerifyCode())) {
                u.setEmail(change.getNewEmail());
                u.setVerifyCode("");
            } else {
                throw new VerificationCodeDoNotMatchException();
            }
        } else {
            throw new PasswordIncorrectExecption();
        }
        this.userRepository.save(u);
    }

    @Override
    public void deleteAccount(String token) {
        User u = this.userRepository.findByEmail(jwtService.extractUsername(token)).orElseThrow(() -> new UsernameNotFoundException(jwtService.extractUsername(token)));
        this.userRepository.delete(u);
    }

    @Override
    public UserDetails loadUserByEmail(String email) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(email).orElseThrow(EmailDoesntExistsException::new);
        if (user == null) {
            throw new UsernameNotFoundException("No user found with email: " + email);
        }
        return new org.springframework.security.core.userdetails.User(
                user.getEmail(),
                user.getPassword(),
                user.getAuthorities()
        );
    }


    @Override
    public String extractEmail(String extractUsername) {
        User u = this.userRepository.findByEmail(extractUsername).orElseThrow(EmailDoesntExistsException::new);
        return u.getEmail();
    }

    @Override
    public String getRoles(String extractUsername) {
        User u = this.userRepository.findByEmail(extractUsername).orElseThrow(EmailDoesntExistsException::new);
        return u.getRole().toString();
    }

    /*@Override
    public void sendVerificationEmail(VerificationDto dto) throws MessagingException, IOException, GeneralSecurityException {
        String rootPath = Thread.currentThread().getContextClassLoader().getResource("").getPath();
        String appConfigPath = rootPath + "application.properties";
        Properties appProps = new Properties();
        appProps.load(new FileInputStream(appConfigPath));
        MailSSLSocketFactory sf = new MailSSLSocketFactory();
        sf.setTrustAllHosts(true);
        appProps.put("mail.smtp .ssl.trust", "*");
        appProps.put("mail.smtp .ssl.socketFactory", sf);
        User u = null;
        if (dto.getToken() != null) {
            String username = jwtService.extractUsername(dto.getToken());
            u = this.userRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException(username));

        } else {
            u = this.userRepository.findByEmail(dto.getEmail()).orElseThrow(EmailDoesntExistsException::new);

        }
        String toAddress = u.getEmail();
        String fromAddress = "the.eventors.app@gmail.com";
        String senderName = "The eventors";
        String subject = "The eventors - The OTP verification code";
        String content = "Dear [[name]],<br/>"
                + "Below is your verification code:<br/>"
                + "<h3>[[code]]</h3>"
                + "Thank you,<br/>"
                + "The eventors.";
       *//* SimpleMailMessage message = new SimpleMailMessage();

        message.setFrom(fromAddress);
        message.setTo(toAddress);
        content = content.replace("[[name]]", u.getUsername());
        Random rand = new Random();
        content = content.replace("[[code]]",String.format("%04d", rand.nextInt(9999)));
        message.setSubject(subject);
        message.setText(content);

        mailSender.send(message);*//*
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);

        helper.setFrom(fromAddress, senderName);
        helper.setTo(toAddress);
        helper.setSubject(subject);

        content = content.replace("[[name]]", u.getUsername());
        Random rand = new Random();
        String code = String.format("%04d", rand.nextInt(9999));
        content = content.replace("[[code]]", code);
        u.setVerifyCode(code);
        this.userRepository.save(u);
        helper.setText(content, true);

        mailSender.send(message);
    }*/

    @Override
    public void updateProfile(UpdateProfileDto dto) {
        String username = jwtService.extractUsername(dto.getToken());
       User u = this.userRepository.findByEmail(username).orElseThrow(() -> new UsernameNotFoundException(username));
        if(dto.getFullName() != ""){
            u.setFullName(dto.getFullName());
        }


        this.userRepository.save(u);
    }
}


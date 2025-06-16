package com.syalux.eduhub.config;

import com.syalux.eduhub.model.*;
import com.syalux.eduhub.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

@Component
@Profile("!test")
public class DataSeeder implements CommandLineRunner {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UniversityRepository universityRepository;
    @Autowired
    private MajorRepository majorRepository;
    @Autowired
    private ApplicationRepository applicationRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {
        Random random = new Random();
        if (userRepository.count() == 0) {
            // --- ADMINS ---
            User admin = new User("admin", "admin@eduhub.com", passwordEncoder.encode("admin123"), Role.ROLE_PLATFORM_ADMIN);
            User staff = new User("staff", "staff@eduhub.com", passwordEncoder.encode("staff123"), Role.ROLE_STAFF);
            User facilityAdmin = new User("facilityadmin", "facility@eduhub.com", passwordEncoder.encode("facility123"), Role.ROLE_FACILITY_ADMIN);
            userRepository.saveAll(Arrays.asList(admin, staff, facilityAdmin));
            // --- 100+ STUDENTS ---
            for (int i = 1; i <= 120; i++) {
                String username = "student" + i;
                String email = "student" + i + "@eduhub.com";
                userRepository.save(new User(username, email, passwordEncoder.encode("student123"), Role.ROLE_STUDENT));
            }
        }
        User facilityAdmin = userRepository.findByEmail("facility@eduhub.com").orElse(null);
        if (universityRepository.count() == 0) {
            // --- 100+ REALISTIC CHINESE UNIVERSITIES & 300+ MAJORS ---
            String[] universityNames = {
                "Tsinghua University", "Peking University", "Fudan University", "Shanghai Jiao Tong University", "Zhejiang University",
                "Nanjing University", "Wuhan University", "Sun Yat-sen University", "Harbin Institute of Technology", "Xi'an Jiaotong University",
                "Beihang University", "Tongji University", "Sichuan University", "Beijing Normal University", "Nankai University",
                "Xiamen University", "Shandong University", "Jilin University", "Central South University", "Huazhong University of Science and Technology",
                "East China Normal University", "Beijing Institute of Technology", "Southeast University", "South China University of Technology", "Tianjin University",
                "Renmin University of China", "Dalian University of Technology", "Hunan University", "Chongqing University", "Northeastern University",
                "University of Science and Technology of China", "Beijing University of Posts and Telecommunications", "China Agricultural University", "Ocean University of China", "Northwestern Polytechnical University",
                "Shanghai University", "Shanghai International Studies University", "Shanghai University of Finance and Economics", "Shanghai University of Electric Power", "Shanghai Maritime University",
                "Guangxi University", "Guangzhou University", "Guangdong University of Technology", "Guangdong University of Foreign Studies", "Guangdong Medical University",
                "Suzhou University", "Nanjing Agricultural University", "Nanjing University of Science and Technology", "Nanjing University of Aeronautics and Astronautics", "Nanjing Medical University",
                "Jiangsu University", "Jiangnan University", "Yangzhou University", "Hefei University of Technology", "Anhui University",
                "Fuzhou University", "Fujian Normal University", "Fujian Medical University", "Fujian Agriculture and Forestry University", "Xiamen University of Technology",
                "Zhejiang University of Technology", "Zhejiang Normal University", "Zhejiang Gongshang University", "Hangzhou Dianzi University", "Ningbo University",
                "Chongqing University of Posts and Telecommunications", "Chongqing Medical University", "Chongqing University of Technology", "Chongqing Jiaotong University", "Chongqing Normal University",
                "Sichuan Agricultural University", "Southwest Jiaotong University", "Southwest University", "Southwest University of Finance and Economics", "Southwest Petroleum University",
                "Yunnan University", "Kunming University of Science and Technology", "Yunnan Normal University", "Yunnan Agricultural University", "Dali University",
                "Guizhou University", "Guizhou Normal University", "Guizhou Medical University", "Guizhou University of Finance and Economics", "Zunyi Medical University",
                "Tibet University", "Tibet Minzu University", "Tibet University of Traditional Tibetan Medicine", "Tibet Agriculture and Animal Husbandry College", "Tibet Vocational Technical College",
                "Xinjiang University", "Xinjiang Medical University", "Xinjiang Normal University", "Xinjiang Agricultural University", "Shihezi University",
                "Inner Mongolia University", "Inner Mongolia University of Technology", "Inner Mongolia Normal University", "Inner Mongolia Agricultural University", "Inner Mongolia Medical University"
            };
            String[] cities = {"Beijing", "Shanghai", "Hangzhou", "Nanjing", "Wuhan", "Guangzhou", "Harbin", "Xi'an", "Chengdu", "Tianjin", "Jinan", "Shenyang", "Changsha", "Chongqing", "Hefei", "Fuzhou", "Kunming", "Guiyang", "Lhasa", "Urumqi", "Hohhot"};
            String[] majorNames = {
                "Computer Science", "Electrical Engineering", "Mechanical Engineering", "Civil Engineering", "Business Administration",
                "Economics", "Law", "Medicine", "Physics", "Mathematics", "Chemistry", "Biology", "Materials Science", "Environmental Science",
                "Architecture", "Finance", "Accounting", "International Relations", "Education", "Psychology", "Sociology", "Political Science",
                "Communication Engineering", "Automation", "Software Engineering", "Data Science", "Artificial Intelligence", "Robotics", "Statistics",
                "Philosophy", "History", "Geography", "Oceanography", "Agronomy", "Forestry", "Veterinary Medicine", "Dentistry", "Nursing"
            };
            for (int i = 0; i < universityNames.length; i++) {
                String city = cities[random.nextInt(cities.length)];
                String logoUrl = "https://placehold.co/200x100?text=" + universityNames[i].replace(" ", "+");
                University uni = new University(
                    universityNames[i],
                    "Top Chinese university: " + universityNames[i] + ". Located in " + city + ".",
                    city,
                    logoUrl,
                    Arrays.asList("Bachelor's degree or above", "HSK 5 or above", "Recommendation letter")
                );
                if (facilityAdmin != null) {
                    uni.setFacilityAdmin(facilityAdmin);
                }
                universityRepository.save(uni);
                // 3-5 majors per university
                int majorCount = 3 + random.nextInt(3);
                for (int j = 0; j < majorCount; j++) {
                    String majorName = majorNames[random.nextInt(majorNames.length)];
                    Major major = new Major();
                    major.setName(majorName);
                    major.setDescription("Study of " + majorName + " at " + universityNames[i]);
                    major.setUniversity(uni);
                    majorRepository.save(major);
                }
            }
        }
        // After all universities are created, ensure zhejiangNormal is set
        // University zhejiangNormal = null;
        // Add more majors for Zhejiang Normal University
        String[] znMajors = {"Education", "Chinese Language", "Mathematics", "Physics", "Chemistry", "Biology", "Computer Science", "Psychology", "History", "Geography"};
        // --- APPLICATIONS ---
        if (applicationRepository.count() == 0) {
            List<User> students = userRepository.findAll();
            List<University> universities = universityRepository.findAll();
            List<Major> majors = majorRepository.findAll();

            // Build lookup maps while session is open
            java.util.Map<String, University> universityNameMap = new java.util.HashMap<>();
            java.util.Map<Long, University> universityIdMap = new java.util.HashMap<>();
            for (University u : universities) {
                universityNameMap.put(u.getName(), u);
                universityIdMap.put(u.getId(), u);
            }
            java.util.Map<Long, java.util.List<Major>> universityMajorsMap = new java.util.HashMap<>();
            for (Major m : majors) {
                Long uniId = m.getUniversity().getId();
                universityMajorsMap.computeIfAbsent(uniId, k -> new java.util.ArrayList<>()).add(m);
            }
            University zhejiangNormal = universityNameMap.get("Zhejiang Normal University");
            // Add more majors for Zhejiang Normal University (after zhejiangNormal is set)
            if (zhejiangNormal != null) {
                for (String majorName : znMajors) {
                    Major major = new Major();
                    major.setName(majorName);
                    major.setDescription("Study of " + majorName + " at Zhejiang Normal University");
                    major.setUniversity(zhejiangNormal);
                    majorRepository.save(major);
                }
            }
            java.util.List<Major> znMajorsList = universityMajorsMap.getOrDefault(zhejiangNormal != null ? zhejiangNormal.getId() : -1L, new java.util.ArrayList<>());

            // Make Zhejiang Normal University have half of all applications
            int totalApplications = 600;
            int znApplications = totalApplications / 2;
            int otherApplications = totalApplications - znApplications;
            // Applications for Zhejiang Normal University
            for (int i = 0; i < znApplications; i++) {
                Application app = new Application();
                User student = students.get(3 + random.nextInt(students.size() - 3));
                Major major = znMajorsList.isEmpty() ? majors.get(random.nextInt(majors.size())) : znMajorsList.get(random.nextInt(znMajorsList.size()));
                app.setStudent(student);
                app.setUniversity(zhejiangNormal);
                app.setMajor(major);
                app.setStatus(ApplicationStatus.values()[random.nextInt(ApplicationStatus.values().length)]);
                app.setApplicationData("{\"essay\":\"Why I want to study " + major.getName() + " at Zhejiang Normal University\",\"gpa\":\"" + (2.5 + random.nextDouble() * 1.5) + "\"}");
                applicationRepository.save(app);
            }
            // Applications for other universities
            for (int i = 0; i < otherApplications; i++) {
                Application app = new Application();
                User student = students.get(3 + random.nextInt(students.size() - 3));
                University selectedUniversity;
                do {
                    selectedUniversity = universities.get(random.nextInt(universities.size()));
                } while (selectedUniversity.getName().equals("Zhejiang Normal University"));
                java.util.List<Major> uniMajors = universityMajorsMap.getOrDefault(selectedUniversity.getId(), new java.util.ArrayList<>());
                Major major = uniMajors.isEmpty() ? majors.get(random.nextInt(majors.size())) : uniMajors.get(random.nextInt(uniMajors.size()));
                app.setStudent(student);
                app.setUniversity(selectedUniversity);
                app.setMajor(major);
                app.setStatus(ApplicationStatus.values()[random.nextInt(ApplicationStatus.values().length)]);
                app.setApplicationData("{\"essay\":\"Why I want to study " + major.getName() + " at " + selectedUniversity.getName() + "\",\"gpa\":\"" + (2.5 + random.nextDouble() * 1.5) + "\"}");
                applicationRepository.save(app);
            }
        }
    }
}

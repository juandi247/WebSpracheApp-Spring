package com.sprache.juandiegodeutsch.admin.admincontroller;

import com.sprache.juandiegodeutsch.admin.adminservices.Admin_Minigame_wordService;
import com.sprache.juandiegodeutsch.admin.adminservices.Admin_TemplateService;
import com.sprache.juandiegodeutsch.admin.adminservices.Admin_UserService;
import com.sprache.juandiegodeutsch.dtos.AdminGetUsersResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor
public class Admin_user_controller {



    private final Admin_UserService adminUserService;





    @GetMapping("/users")
    public ResponseEntity<Page<AdminGetUsersResponseDTO>> getAllUsers(
            @RequestParam(defaultValue = "0") int page
    ) {
        int fixedPageSize = 10;
        Page<AdminGetUsersResponseDTO> users = adminUserService.getAllUsers(page, fixedPageSize);
        return ResponseEntity.ok(users);
    }
}

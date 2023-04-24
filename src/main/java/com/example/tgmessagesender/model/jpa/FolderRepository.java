package com.example.tgmessagesender.model.jpa;

import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface FolderRepository extends CrudRepository<Folder, Long> {

    public List<Folder> getFoldersByIsDelete(boolean isDelete);

    public Folder getFoldersByNameIsAndIsDelete(String name, boolean isDelete);
}

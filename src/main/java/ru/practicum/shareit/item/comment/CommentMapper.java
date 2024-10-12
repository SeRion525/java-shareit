package ru.practicum.shareit.item.comment;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.practicum.shareit.item.comment.dto.CommentCreateDto;
import ru.practicum.shareit.item.comment.dto.CommentDto;
import ru.practicum.shareit.item.comment.model.Comment;

@Mapper(componentModel = "spring")
public interface CommentMapper {
    Comment toComment(CommentCreateDto commentCreateDto);

    @Mapping(target = "authorName", source = "author.name")
    @Mapping(target = "itemName", source = "item.name")
    CommentDto toCommentDto(Comment comment);
}

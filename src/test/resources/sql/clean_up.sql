SET FOREIGN_KEY_CHECKS = 0;

/*멤버 관련*/
truncate table favorite_categories;
truncate table profile_images;
truncate table follows;
truncate table members;

truncate table space_images;
truncate table favorites;
truncate table comments;
truncate table invitations;
truncate table scraps;
truncate table space_member;
truncate table spaces;

truncate table link_tags;
truncate table tags;
truncate table likes;
truncate table link_view_histories;
truncate table links;

truncate table notifications;
SET FOREIGN_KEY_CHECKS = 1;

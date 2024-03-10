DROP INDEX idx_spaces_created_at ON spaces;
DROP INDEX idx_spaces_favorite_count ON spaces;

CREATE INDEX idx_spaces_favorite_count_id ON spaces (favorite_count desc, id desc);

INSERT INTO
    DEVICE_TYPE(ID, NAME)
VALUES
    (default, 'linux'),     -- ID = 1
    (default, 'mac'),       -- ID = 2
    (default, 'windows'),   -- ID = 3
    (default, 'android')    -- ID = 4
;


INSERT INTO
    SERVICE(ID, NAME, PRICE, APPLY_TO_ALL)
VALUES
    (default, 'Default', 4.00, true),                   -- ID = 1
    (default, 'Antivirus for Mac', 7.00, false),        -- ID = 2
    (default, 'Antivirus for Windows', 5.00, false),    -- ID = 3
    (default, 'Backup', 3.00, false),                   -- ID = 4
    (default, 'Screen Share', 1.00, false)              -- ID = 5
;

INSERT INTO
    SERVICE_IN_DEVICE_TYPE(DEVICE_TYPE_ID, SERVICE_ID)
VALUES
    -- Config for linux
    (SELECT ID FROM DEVICE_TYPE WHERE NAME = 'linux', SELECT ID FROM SERVICE WHERE NAME = 'Backup'),
    (SELECT ID FROM DEVICE_TYPE WHERE NAME = 'linux', SELECT ID FROM SERVICE WHERE NAME = 'Screen Share'),
    -- Config for mac
    (SELECT ID FROM DEVICE_TYPE WHERE NAME = 'mac', SELECT ID FROM SERVICE WHERE NAME = 'Antivirus for Mac'),
    (SELECT ID FROM DEVICE_TYPE WHERE NAME = 'mac', SELECT ID FROM SERVICE WHERE NAME = 'Backup'),
    (SELECT ID FROM DEVICE_TYPE WHERE NAME = 'mac', SELECT ID FROM SERVICE WHERE NAME = 'Screen Share'),
    -- Config for windows
    (SELECT ID FROM DEVICE_TYPE WHERE NAME = 'windows', SELECT ID FROM SERVICE WHERE NAME = 'Antivirus for Windows'),
    (SELECT ID FROM DEVICE_TYPE WHERE NAME = 'windows', SELECT ID FROM SERVICE WHERE NAME = 'Backup'),
    (SELECT ID FROM DEVICE_TYPE WHERE NAME = 'windows', SELECT ID FROM SERVICE WHERE NAME = 'Screen Share'),
    -- Config for android
    (SELECT ID FROM DEVICE_TYPE WHERE NAME = 'android', SELECT ID FROM SERVICE WHERE NAME = 'Backup'),
    (SELECT ID FROM DEVICE_TYPE WHERE NAME = 'android', SELECT ID FROM SERVICE WHERE NAME = 'Screen Share')
;


INSERT INTO
    DEVICE(ID, NAME, APP_USER_ID, DEVICE_TYPE_ID)
VALUES
    (default, 'Mac - 1', 0, SELECT ID FROM DEVICE_TYPE WHERE NAME = 'mac'),         -- MAC(2)      -- ID = 1
    (default, 'Mac - 2', 0, SELECT ID FROM DEVICE_TYPE WHERE NAME = 'mac'),         -- MAC(2)      -- ID = 2
    (default, 'Mac - 3', 0, SELECT ID FROM DEVICE_TYPE WHERE NAME = 'mac'),         -- MAC(2)      -- ID = 3
    (default, 'Windows - 1', 0, SELECT ID FROM DEVICE_TYPE WHERE NAME = 'windows'), -- Windows(3)  -- ID = 4
    (default, 'Windows - 2', 0, SELECT ID FROM DEVICE_TYPE WHERE NAME = 'windows'); -- Windows(3)  -- ID = 5


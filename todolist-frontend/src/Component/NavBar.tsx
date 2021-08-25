import React, {useState} from "react";
import AppBar from "@material-ui/core/AppBar";
import Toolbar from "@material-ui/core/Toolbar";
import IconButton from "@material-ui/core/IconButton";
import Typography from "@material-ui/core/Typography";
import Button from "@material-ui/core/Button";
import makeStyles from "@material-ui/core/styles/makeStyles";
import {Drawer, ListItemText} from "@material-ui/core";
import List from "@material-ui/core/List";
import ListItem from "@material-ui/core/ListItem";
import ListItemIcon from "@material-ui/core/ListItemIcon";
import TodayIcon from '@material-ui/icons/Today';
import MenuIcon from '@material-ui/icons/Menu';
import ListIcon from '@material-ui/icons/List';
import DateRangeIcon from '@material-ui/icons/DateRange';

const useStyles = makeStyles((theme: any) => ({
    root: {
        flexGrow: 1,
    },
    menuButton: {
        marginRight: theme.spacing(2),
    },
    title: {
        flexGrow: 1,
    },
}));

const doLogout = () => {
    return fetch('/api/rest/v1/logout')
        .then(() => {
            window.location.href = '/';
        });
};

interface NavBarProps {
    onListDateChange: (endDate: Date, title: string) => void;
}

const NavBar = (props: NavBarProps) => {
    const classes: any = useStyles();
    const [showDrawer, setShowDrawer] = useState<boolean>(false);
    const selectDate = (plusDays: number | null) => {
        if (plusDays === null) {
            const endDate: Date = new Date();
            endDate.setDate(endDate.getDate() + 36500);
            props.onListDateChange(endDate, 'All Events');
        } else {
            const endDate: Date = new Date();
            endDate.setDate(endDate.getDate() + plusDays);
            endDate.setHours(0);
            endDate.setMinutes(0);
            endDate.setSeconds(0);
            props.onListDateChange(endDate, (plusDays === 1 ? "Today's Events" : "This Week's Events"));
        }
        setShowDrawer(false);
    };
    return (
        <>
            <AppBar position="fixed">
                <Toolbar>
                    <IconButton edge="start" onClick={() => setShowDrawer(true)} className={classes.menuButton} color="inherit" aria-label="menu">
                        <MenuIcon />
                    </IconButton>
                    <Typography variant="h6" className={classes.title}>
                        ToDoList
                    </Typography>
                    <Button color="inherit" onClick={() => doLogout()}>Logout</Button>
                </Toolbar>
            </AppBar>
            <Drawer anchor={'left'} open={showDrawer} onClose={() => setShowDrawer(false)}>
                <List>
                    <ListItem button key={'today'} onClick={() => selectDate(1)}>
                        <ListItemIcon>
                            <TodayIcon />
                        </ListItemIcon>
                        <ListItemText>
                            Today's Events
                        </ListItemText>
                    </ListItem>
                    <ListItem button key={'This Week'} onClick={() => selectDate(7)}>
                        <ListItemIcon>
                            <DateRangeIcon />
                        </ListItemIcon>
                        <ListItemText>
                            This Week's Events
                        </ListItemText>
                    </ListItem>
                    <ListItem button key={'all'} onClick={() => selectDate(null)}>
                        <ListItemIcon>
                            <ListIcon />
                        </ListItemIcon>
                        <ListItemText>
                            All Events
                        </ListItemText>
                    </ListItem>
                </List>
            </Drawer>
        </>
    );
};
export default NavBar;
import {DropdownMenu, DropdownMenuContent, DropdownMenuItem, DropdownMenuTrigger} from "@/components/ui/dropdown-menu";
import {NavigationMenu, NavigationMenuItem, NavigationMenuLink, NavigationMenuList} from "@/components/ui/navigation-menu";
import {Sheet, SheetClose, SheetContent, SheetTrigger} from "@/components/ui/sheet";
import {Avatar, AvatarFallback, AvatarImage} from "@/components/ui/avatar";
import {BriefcaseMedical, ChartPie, ChevronDown, LogIn, LogOut, Menu, User} from "lucide-react";
import {Link, useMatch, useNavigate} from "react-router-dom";
import {cn} from "@/lib/utils";
import React, {useEffect, useMemo, useState} from "react";
import {useAuth} from "@/hooks/useAuth";

type UserRole = "ANON" | "PATIENT" | "DOCTOR";

type NavItem = {
    to: string;
    label: string;
    end?: boolean;
};

function HeaderNavLink({
                           to,
                           end,
                           children
                       }: {
    to: string;
    end?: boolean;
    children: React.ReactNode;
}) {
    const match = useMatch({ path: to, end: end });
    return (
        <NavigationMenuLink asChild className={cn(navItem, match && navItemActive)}>
            <Link to={to}>{children}</Link>
        </NavigationMenuLink>
    );
}

function SheetNavLink({
                          to,
                          end,
                          children
                      }: {
    to: string;
    end?: boolean;
    children: React.ReactNode;
}) {
    const match = useMatch({ path: to, end: end });
    return (
        <SheetClose asChild>
            <Link to={to} className={cn(mobileNavLink, match && mobileNavLinkActive)}>
                {children}
            </Link>
        </SheetClose>
    );
}

const header =
    "fixed top-0 left-0 w-full shadow-[var(--shadow-md)] py-8 leading-[1.6] bg-white z-50";
const headerContainer = "flex items-center justify-between w-full max-w-6xl px-5 mx-auto";
const logo =
    "flex items-center text-[var(--primary-color)] transition-transform duration-300 ease-[ease] hover:scale-105";
const nav = "font-medium";
const navDesktop = "hidden md:block";
const navList = "flex gap-8 items-center";
const navItem =
    "text-base cursor-pointer hover:text-[var(--primary-color)] hover:bg-transparent " +
    "relative px-0 py-1 font-medium transition-colors duration-300 " +
    "focus:bg-transparent active:bg-transparent focus:text-[var(--primary-color)] " +
    "after:content-[''] after:absolute after:left-0 after:bottom-0 after:h-0.5 after:w-0 after:bg-[var(--primary-color)] " +
    "after:transition-[width] after:duration-300 hover:after:w-full";
const navItemActive = "text-[var(--primary-color)] font-semibold after:w-full";

function deriveUserRole(isAuthenticated: boolean, role?: string | null): UserRole {
    if (!isAuthenticated) return "ANON";
    return role === "ROLE_DOCTOR" ? "DOCTOR" : "PATIENT";
}

function buildNavItems(role: UserRole): NavItem[] {
    const canFindDoctors = role !== "DOCTOR";

    return [
        {to: "/", label: "Home", end: true},
        ...(canFindDoctors ? [{to: "/search", label: "Find Doctors"} as NavItem] : []),
        {to: "/about-us", label: "About Us"}
    ];
}

function Header() {
    const auth = useAuth();

    const isLoggedIn = auth.isAuthenticated;
    const userRole = deriveUserRole(isLoggedIn, auth.role);

    const navItems = useMemo(() => buildNavItems(userRole), [userRole]);

    const [sheetOpen, setSheetOpen] = useState(false);
    const [dropdownOpen, setDropdownOpen] = useState(false);

    useEffect(() => {
        if (typeof window === "undefined") return;

        const mql = window.matchMedia("(min-width: 768px)");

        const onChange = (e: MediaQueryListEvent) => {
            if (e.matches) setSheetOpen(false);
            if (!e.matches) setDropdownOpen(false);
        };

        if (mql.matches) setSheetOpen(false);
        if (!mql.matches) setDropdownOpen(false);

        const mqlAny = mql as unknown as {
            addEventListener?: (type: "change", listener: (e: MediaQueryListEvent) => void) => void;
            removeEventListener?: (type: "change", listener: (e: MediaQueryListEvent) => void) => void;
            addListener?: (listener: (e: MediaQueryListEvent) => void) => void;
            removeListener?: (listener: (e: MediaQueryListEvent) => void) => void;
        };

        if (mqlAny.addEventListener && mqlAny.removeEventListener) {
            mqlAny.addEventListener("change", onChange);
            return () => mqlAny.removeEventListener!("change", onChange);
        }

        mqlAny.addListener?.(onChange);
        return () => mqlAny.removeListener?.(onChange);
    }, []);

    return (
        <div className={header}>
            <div className={headerContainer}>
                <a href="/" className={logo}>
                    <h1 className="block text-5xl font-bold no-underline">Vitae</h1>
                </a>

                <NavigationMenu className={cn(nav, navDesktop)}>
                    <NavigationMenuList className={navList}>
                        {navItems.map((item) => (
                            <NavigationMenuItem key={item.to}>
                                <HeaderNavLink to={item.to} end={item.end}>
                                    {item.label}
                                </HeaderNavLink>
                            </NavigationMenuItem>
                        ))}
                    </NavigationMenuList>
                </NavigationMenu>

                {isLoggedIn ? (
                    <LoggedInComponent
                        userRole={userRole}
                        open={dropdownOpen}
                        onOpenChange={setDropdownOpen}
                    />
                ) : (
                    <NotLoggedInComponent open={dropdownOpen} onOpenChange={setDropdownOpen} />
                )}

                <SheetComponent
                    open={sheetOpen}
                    onOpenChange={setSheetOpen}
                    isLoggedIn={isLoggedIn}
                    userRole={userRole}
                    navItems={navItems}
                />
            </div>
        </div>
    );
}

const mobileTrigger =
    "md:hidden inline-flex items-center justify-center h-11 w-11 rounded-md border border-[var(--primary-color)] text-[var(--primary-color)] hover:text-white hover:border-[var(--primary-dark)] hover:bg-[var(--primary-dark)] cursor-pointer";
const sheetContent = "w-screen max-w-none p-6 border-r-0";
const mobileNav = "mt-6 flex flex-col gap-4";
const mobileNavLink =
    "text-base cursor-pointer hover:text-[var(--primary-color)] hover:bg-transparent " +
    "relative w-full px-0 py-1 font-medium transition-colors duration-300 " +
    "after:content-[''] after:absolute after:left-0 after:bottom-0 after:h-0.5 after:w-0 after:bg-[var(--primary-color)] " +
    "after:transition-[width] after:duration-300 hover:after:w-full";
const mobileNavLinkActive = "text-[var(--primary-color)] font-semibold after:w-full";
const mobileSectionTitle = "mt-2 text-sm font-semibold text-[var(--text-light)]";

function SheetComponent({
                            open,
                            onOpenChange,
                            isLoggedIn,
                            userRole,
                            navItems
                        }: {
    open: boolean;
    onOpenChange: (open: boolean) => void;
    isLoggedIn: boolean;
    userRole: UserRole;
    navItems: NavItem[];
}) {
    return (
        <Sheet open={open} onOpenChange={onOpenChange}>
            <SheetTrigger className={mobileTrigger} aria-label="Open menu">
                <Menu className="h-5 w-5" />
            </SheetTrigger>
            <SheetContent side="left" className={sheetContent}>
                <nav className={mobileNav}>
                    {navItems.map((item) => (
                        <SheetNavLink key={item.to} to={item.to} end={item.end}>
                            {item.label}
                        </SheetNavLink>
                    ))}

                    <div className={mobileSectionTitle}>Account</div>
                    {isLoggedIn ? <SheetLoggedInComponent userRole={userRole} /> : <SheetNotLoggedInComponent />}
                </nav>
            </SheetContent>
        </Sheet>
    );
}

const btnBase =
    "inline-flex items-center justify-center text-base font-medium leading-[1.5] px-6 h-11 rounded-md !border-2 transition-colors transition-transform duration-300 ease-in-out";
const btnOutline = `${btnBase} bg-transparent text-[var(--primary-color)] border-[var(--primary-color)] hover:bg-[var(--primary-dark)] hover:border-[var(--primary-dark)] hover:text-[var(--white)]`;
const btnFilled = `${btnBase} bg-[var(--primary-color)] text-[var(--white)] border-[var(--primary-color)] hover:bg-[var(--primary-dark)] hover:border-[var(--primary-dark)] cursor-pointer`;
const dropDownItem =
    "flex items-center gap-2 text-base cursor-pointer data-[highlighted]:text-[var(--primary-color)] data-[highlighted]:bg-transparent";
const authDesktop = "hidden md:flex gap-3 items-center";

function NotLoggedInComponent({
                                  open,
                                  onOpenChange
                              }: {
    open: boolean;
    onOpenChange: (open: boolean) => void;
}) {
    return (
        <div className={authDesktop}>
            <Link to="/login" className={btnOutline}>
                Login
            </Link>
            <DropdownMenu open={open} onOpenChange={onOpenChange}>
                <DropdownMenuTrigger className={btnFilled}>
                    Register
                    <ChevronDown className="h-4 w-4 ml-1" />
                </DropdownMenuTrigger>
                {/*TODO: que lleven a distintos paths*/}
                <DropdownMenuContent>
                    <DropdownMenuItem className={dropDownItem}>
                        <Link to="/register" className={dropDownItem}>
                            <User className="text-inherit" />
                            Register
                        </Link>
                    </DropdownMenuItem>
                    <DropdownMenuItem className={dropDownItem}>
                        <Link to="/register" className={dropDownItem}>
                            <BriefcaseMedical className="text-inherit" />
                            Are you a Doctor?
                        </Link>
                    </DropdownMenuItem>
                </DropdownMenuContent>
            </DropdownMenu>
        </div>
    );
}

const loggedInContainer =
    "flex items-center gap-2 cursor-pointer hover:bg-[var(--gray-100)] rounded-sm px-3 py-2";
const headerAvatar = "w-8 h-8";
const userName = "max-w-[120px] truncate text-base text-[var(--text-color)]";
const logoutItem =
    "flex items-center gap-2 text-base text-[var(--danger)] cursor-pointer data-[highlighted]:text-[var(--danger-dark)] data-[highlighted]:bg-[var(--danger-light)]";

type LoggedInRole = Exclude<UserRole, "ANON">;

function LoggedInComponent({
                               userRole,
                               open,
                               onOpenChange
                           }: {
    userRole: UserRole;
    open: boolean;
    onOpenChange: (open: boolean) => void;
}) {
    if (userRole === "ANON") return null;

    return (
        <div className={authDesktop}>
            <DropdownMenu open={open} onOpenChange={onOpenChange}>
                <DropdownMenuTrigger className={loggedInContainer}>
                    <Avatar className={headerAvatar}>
                        <AvatarImage src="https://picsum.photos/200" />
                        <AvatarFallback>JD</AvatarFallback>
                    </Avatar>
                    <p className={userName}>John Doe</p>
                    <ChevronDown className="h-4 w-4 text-[var(--text-color)]" />
                </DropdownMenuTrigger>

                <LoggedInDropdown userRole={userRole} />
            </DropdownMenu>
        </div>
    );
}

function LoggedInDropdown({ userRole }: { userRole: LoggedInRole }) {
    const auth = useAuth();
    const navigate = useNavigate();

    const logout = () => {
        auth.logout();
        navigate("/");
    };

    const items =
        userRole === "DOCTOR"
            ? [
                { to: "/doctor/dashboard", label: "Doctor Dashboard", icon: ChartPie },
                { to: "/profile", label: "Public Profile", icon: User }
            ]
            : [{ to: "/patient/dashboard", label: "Dashboard", icon: ChartPie }];

    return (
        <DropdownMenuContent>
            {items.map((item) => (
                <DropdownMenuItem key={item.to} className={dropDownItem}>
                    <Link to={item.to} className={dropDownItem}>
                        <item.icon className="text-inherit" />
                        {item.label}
                    </Link>
                </DropdownMenuItem>
            ))}
            <DropdownMenuItem onClick={logout} className={logoutItem}>
                <LogOut className="text-inherit" />
                Log Out
            </DropdownMenuItem>
        </DropdownMenuContent>
    );
}

const sheetContainer = "flex flex-col gap-4";
const mobileIconContainer = "flex items-center gap-1";
const mobileIcon = "h-5 w-5";

function SheetNotLoggedInComponent() {
    return (
        <div className={sheetContainer}>
            <SheetNavLink to="/login">
                <div className={mobileIconContainer}>
                    <LogIn className={mobileIcon} />
                    Login
                </div>
            </SheetNavLink>
            <SheetNavLink to="/register">
                <div className={mobileIconContainer}>
                    <User className={mobileIcon} />
                    Register
                </div>
            </SheetNavLink>
            <SheetNavLink to="/register">
                <div className={mobileIconContainer}>
                    <BriefcaseMedical className={mobileIcon} />
                    Are You a Doctor?
                </div>
            </SheetNavLink>
        </div>
    );
}

const avatarContainer = "flex items-center gap-2";
const avatarSheetImage = "mb-2";
const userNameSheet = "text-base font-medium";
const logoutHover =
    "text-base cursor-pointer hover:text-[var(--danger-dark)] hover:bg-transparent " +
    "relative w-full px-0 py-1 font-medium transition-colors duration-300 " +
    "after:content-[''] after:absolute after:left-0 after:bottom-0 after:h-0.5 after:w-0 after:bg-[var(--danger-dark)] " +
    "after:transition-[width] after:duration-300 hover:after:w-full";

function SheetLoggedInComponent({ userRole }: { userRole: UserRole }) {
    const auth = useAuth();
    const navigate = useNavigate();

    const logout = () => {
        auth.logout();
        navigate("/");
    };

    if (userRole === "ANON") return null;

    return (
        <div className={sheetContainer}>
            <div className={avatarContainer}>
                <Avatar className={avatarSheetImage}>
                    <AvatarImage src="https://picsum.photos/200" />
                    <AvatarFallback>JD</AvatarFallback>
                </Avatar>
                <p className={userNameSheet}>John Doe</p>
            </div>

            {userRole === "DOCTOR" ? (
                <>
                    <SheetNavLink to="/doctor/dashboard">
                        <div className={mobileIconContainer}>
                            <ChartPie className={mobileIcon} />
                            Doctor Dashboard
                        </div>
                    </SheetNavLink>

                    <SheetNavLink to="/profile">
                        <div className={mobileIconContainer}>
                            <User className={mobileIcon} />
                            Public Profile
                        </div>
                    </SheetNavLink>
                    <div onClick={logout} className={logoutItem + " " + logoutHover}>
                        <LogOut className="text-inherit" />
                        <p>Log Out</p>
                    </div>
                </>
            ) : (
                <>
                    <SheetNavLink to="/patient/dashboard">
                        <div className={mobileIconContainer}>
                            <ChartPie className={mobileIcon} />
                            Dashboard
                        </div>
                    </SheetNavLink>
                    <div onClick={logout} className={logoutItem + " " + logoutHover}>
                        <LogOut className="text-inherit" />
                        <p>Log Out</p>
                    </div>
                </>
            )}
        </div>
    );
}

export default Header;
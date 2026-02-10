import {DropdownMenu, DropdownMenuContent, DropdownMenuItem, DropdownMenuTrigger} from "@/components/ui/dropdown-menu";
import {NavigationMenu, NavigationMenuItem, NavigationMenuLink, NavigationMenuList} from "@/components/ui/navigation-menu";
import {Sheet, SheetClose, SheetContent, SheetTrigger} from "@/components/ui/sheet";
import {Avatar, AvatarFallback, AvatarImage} from "@/components/ui/avatar";
import {BriefcaseMedical, ChartPie, ChevronDown, LogIn, LogOut, Menu, User} from "lucide-react";
import {Link, useMatch} from "react-router-dom";
import {cn} from "@/lib/utils";
import React, {useEffect, useMemo, useState} from "react";
import {useAuth} from "@/hooks/useAuth";
import {
    Dialog,
    DialogClose,
    DialogContent,
    DialogDescription,
    DialogFooter,
    DialogHeader,
    DialogTitle,
} from "@/components/ui/dialog.tsx";

import {Button} from "@/components/ui/button.tsx";
import {useTranslation} from "react-i18next";
import {useDoctorImageUrl} from "@/hooks/useDoctors.ts";
import {useUser} from "@/hooks/useUser.ts";
import OfflineBanner from "@/components/OfflineBanner.tsx";
import {Skeleton} from "@/components/ui/skeleton.tsx";

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
    " bg-white relative h-full py-8 shadow-(--shadow-md) z-20 ";
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

function buildNavItems(role: UserRole, t: (key: string) => string): NavItem[] {
    const canFindDoctors = role !== "DOCTOR";

    return [
        {to: "/", label: t("header.home"), end: true},
        ...(canFindDoctors ? [{to: "/search", label: t("header.search")} as NavItem] : []),
        {to: "/about-us", label: t("header.about")}
    ];
}

function Header() {
    const auth = useAuth();
    const { t } = useTranslation();

    const isLoggedIn = auth.isAuthenticated;
    const userRole = deriveUserRole(isLoggedIn, auth.role);
    const isDoctor = (isLoggedIn && userRole === "DOCTOR") ? true : undefined;
    const doctorId = isDoctor ? auth.userId : undefined;
    const { url: getDoctorImgUrl, isLoading: imageIsLoading } = useDoctorImageUrl(doctorId);
    const doctorImgUrl = getDoctorImgUrl;

    const user = useUser(auth.role, auth.userId)

    const isLoading = user?.isLoading || imageIsLoading;
    const displayName = user?.data ? `${user.data.name} ${user.data.lastName}` : auth.email ?? "";

    const avatarFallbackText = (() => {
        if (user?.data) {
            const name = user.data.name.trim()?.[0] ?? "";
            const lastName = user.data.lastName.trim()?.[0] ?? "";
            return (name + lastName).toUpperCase() || "U";
        }
        return auth.email?.trim()?.[0]?.toUpperCase() ?? "U";
    })();

    const navItems = useMemo(() => buildNavItems(userRole, t), [userRole, t]);

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
        <div className="fixed top-0 left-0 w-full leading-[1.6] z-50">
            <div className={header}>
                <div className={headerContainer}>
                    <Link to="/" className={logo}>
                        <h1 className="block text-5xl font-bold no-underline">Vitae</h1>
                    </Link>

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
                        isLoading ? (
                            <LoadingComponent/>
                            ) : (
                        <LoggedInComponent
                            userRole={userRole}
                            open={dropdownOpen}
                            onOpenChange={setDropdownOpen}
                            displayName={displayName}
                            avatarFallbackText={avatarFallbackText}
                            doctorImgUrl={doctorImgUrl}
                        />)
                    ) : (
                        <NotLoggedInComponent open={dropdownOpen} onOpenChange={setDropdownOpen} />
                    )}

                    <SheetComponent
                        open={sheetOpen}
                        onOpenChange={setSheetOpen}
                        isLoggedIn={isLoggedIn}
                        userRole={userRole}
                        navItems={navItems}
                        displayName={displayName}
                        avatarFallbackText={avatarFallbackText}
                        doctorImgUrl={doctorImgUrl}
                    />
                </div>
            </div>
            <OfflineBanner/>
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
                            navItems,
                            displayName,
                            avatarFallbackText,
                            doctorImgUrl
                        }: {
    open: boolean;
    onOpenChange: (open: boolean) => void;
    isLoggedIn: boolean;
    userRole: UserRole;
    navItems: NavItem[];
    displayName: string;
    avatarFallbackText: string;
    doctorImgUrl: string | null;
}) {
    const { t } = useTranslation();

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
                    <div className={mobileSectionTitle}>{t("header.account")}</div>
                    {isLoggedIn ? <SheetLoggedInComponent
                        userRole={userRole}
                        displayName={displayName}
                        avatarFallbackText={avatarFallbackText}
                        doctorImgUrl={doctorImgUrl}
                    /> : <SheetNotLoggedInComponent />}
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
    const { t } = useTranslation();
    return (
        <div className={authDesktop}>
            <Link to="/login" className={btnOutline}>
                {t("header.login")}
            </Link>
            <DropdownMenu open={open} onOpenChange={onOpenChange}>
                <DropdownMenuTrigger className={btnFilled}>
                    {t("header.register")}
                    <ChevronDown className="h-4 w-4 ml-1" />
                </DropdownMenuTrigger>
                <DropdownMenuContent>
                    <DropdownMenuItem onClick={() => onOpenChange(false)} className={dropDownItem}>
                        <Link to="/register?type=patient" className={dropDownItem}>
                            <User className="text-inherit" />
                            {t("header.register_patient")}
                        </Link>
                    </DropdownMenuItem>
                    <DropdownMenuItem onClick={() => onOpenChange(false)} className={dropDownItem}>
                        <Link to="/register?type=doctor" className={dropDownItem}>
                            <BriefcaseMedical className="text-inherit" />
                            {t("header.register_doctor")}
                        </Link>
                    </DropdownMenuItem>
                </DropdownMenuContent>
            </DropdownMenu>
        </div>
    );
}

const loggedInContainer =
    "flex items-center gap-2 cursor-pointer hover:bg-[var(--gray-100)] rounded-lg px-3 py-2";
const headerAvatar = "w-8 h-8";
const userName = "max-w-[120px] truncate text-base text-[var(--text-color)]";
const chevronDown =
    "h-4 w-4 text-[var(--text-color)]";
const logoutItem =
    "flex items-center gap-2 text-base text-[var(--danger)] cursor-pointer data-[highlighted]:text-[var(--danger-dark)] data-[highlighted]:bg-[var(--danger-light)]";

type LoggedInRole = Exclude<UserRole, "ANON">;

const dialogHeader =
    "font-bold text-xl text-[var(--text-color)]";
const dialogText =
    "text-[var(--text-light)] text-lg font-normal";
const dialogFooter =
    "mt-2";
const dialogCancel =
    "bg-white text-(--gray-600) border border-(--gray-400) hover:bg-(--gray-100) hover:border-(--gray-500-) hover:text-(--text-color) cursor-pointer transition-colors";
const dialogConfirm =
    "text-white bg-[var(--danger)] border border-[var(--danger)] hover:text-white hover:bg-[var(--danger-dark)] hover:border hover:border-[var(--danger-dark)] cursor-pointer";

function LoggedInComponent({
                               userRole,
                               open,
                               onOpenChange,
                               displayName,
                               avatarFallbackText,
                               doctorImgUrl
                           }: {
    userRole: UserRole;
    open: boolean;
    onOpenChange: (open: boolean) => void;
    displayName: string;
    avatarFallbackText: string;
    doctorImgUrl: string | null;
}) {
    if (userRole === "ANON") return null;

    const { t } = useTranslation();
    const auth = useAuth();
    const [logoutOpen, setLogoutOpen] = useState(false);

    const logout = () => {
        onOpenChange(false);
        setLogoutOpen(false);
        auth.logout();
    };

    return (
        <div className={authDesktop}>
            <DropdownMenu open={open} onOpenChange={onOpenChange}>
                <DropdownMenuTrigger className={loggedInContainer}>
                    <Avatar className={headerAvatar}>
                        <AvatarImage src={doctorImgUrl ?? undefined}/>
                        <AvatarFallback>{avatarFallbackText}</AvatarFallback>
                    </Avatar>
                    <p className={userName}>{displayName}</p>
                    <ChevronDown className={open ? (chevronDown + " rotate-180 transition-transform") : (chevronDown + " transition-transform")} />
                </DropdownMenuTrigger>

                <LoggedInDropdown
                    userRole={userRole}
                    closeDropdown={() => onOpenChange(false)}
                    onLogoutClick={() => {
                        onOpenChange(false);
                        setLogoutOpen(true);
                    }}
                    userId={auth.userId}
                />
            </DropdownMenu>

            <Dialog
                open={logoutOpen}
                onOpenChange={(next) => {
                    setLogoutOpen(next);
                    if (!next) onOpenChange(false);
                }}
            >
                <DialogContent onOpenAutoFocus={(e) => e.preventDefault()}>
                    <DialogHeader className={dialogHeader}>
                        <DialogTitle>
                            {t("header.logout.title")}
                        </DialogTitle>

                        <DialogDescription className={dialogText}>
                            {t("header.logout.text")}
                        </DialogDescription>
                    </DialogHeader>

                    <DialogFooter className={dialogFooter}>
                        <DialogClose asChild>
                            <Button className={dialogCancel}>{t("header.logout.cancel")}</Button>
                        </DialogClose>
                        <Button onClick={logout} type="button" className={dialogConfirm}>
                            {t("header.logout.confirm")}
                        </Button>
                    </DialogFooter>
                </DialogContent>
            </Dialog>
        </div>
    );
}

function LoadingComponent() {
    return (
        <div className={authDesktop}>
            <Skeleton className={`${headerAvatar} rounded-full`} />
            <Skeleton className="w-[140px] h-6" />
        </div>
    );
}

function LoggedInDropdown({
                              userRole,
                              onLogoutClick,
                              closeDropdown,
                              userId
                          }: {
    userRole: LoggedInRole;
    onLogoutClick: () => void;
    closeDropdown: () => void;
    userId?: string;
}) {
    const { t } = useTranslation();
    const items =
        userRole === "DOCTOR"
            ? [
                { to: "/doctor/dashboard", label: t("header.doctor.dashboard"), icon: ChartPie },
                { to: `/search/${userId}`, label: t("header.doctor.profile"), icon: User },
            ]
            : [{ to: "/patient/dashboard", label: t("header.patient.dashboard"), icon: ChartPie }];

    return (
        <DropdownMenuContent>
            {items.map((item) => (
                <DropdownMenuItem
                    key={item.to}
                    className={dropDownItem}
                    asChild
                    onSelect={() => closeDropdown()}
                >
                    <Link to={item.to} className={dropDownItem}>
                        <item.icon className="text-inherit" />
                        {item.label}
                    </Link>
                </DropdownMenuItem>
            ))}

            <DropdownMenuItem
                className={logoutItem}
                onSelect={(e) => {
                    e.preventDefault();
                    onLogoutClick();
                }}
            >
                <LogOut className="text-inherit" />
                {t("header.logout.confirm")}
            </DropdownMenuItem>
        </DropdownMenuContent>
    );
}

const sheetContainer = "flex flex-col gap-4";
const mobileIconContainer = "flex items-center gap-1";
const mobileIcon = "h-5 w-5";

function SheetNotLoggedInComponent() {
    const { t } = useTranslation();
    return (
        <div className={sheetContainer}>
            <SheetNavLink to="/login">
                <div className={mobileIconContainer}>
                    <LogIn className={mobileIcon} />
                    {t("header.login")}
                </div>
            </SheetNavLink>
            <SheetNavLink to="/register">
                <div className={mobileIconContainer}>
                    <User className={mobileIcon} />
                    {t("header.register")}
                </div>
            </SheetNavLink>
            <SheetNavLink to="/register">
                <div className={mobileIconContainer}>
                    <BriefcaseMedical className={mobileIcon} />
                    {t("header.search")}
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

function SheetLoggedInComponent({ userRole, displayName, avatarFallbackText, doctorImgUrl }: {
    userRole: UserRole,
    displayName: string,
    avatarFallbackText: string,
    doctorImgUrl: string | null,
}) {
    const { t } = useTranslation();
    const auth = useAuth();
    const logout = () => {
        auth.logout();
    };

    if (userRole === "ANON") return null;

    return (
        <div className={sheetContainer}>
            <div className={avatarContainer}>
                <Avatar className={avatarSheetImage}>
                    <AvatarImage src={doctorImgUrl ?? undefined} />
                    <AvatarFallback>{avatarFallbackText}</AvatarFallback>
                </Avatar>
                <p className={userNameSheet}>{displayName}</p>
            </div>

            {userRole === "DOCTOR" ? (
                <>
                    <SheetNavLink to="/doctor/dashboard">
                        <div className={mobileIconContainer}>
                            <ChartPie className={mobileIcon}/>
                            {t("header.doctor.dashboard")}
                        </div>
                    </SheetNavLink>

                    <SheetNavLink to="/profile">
                        <div className={mobileIconContainer}>
                            <User className={mobileIcon}/>
                            {t("header.doctor.profile")}
                        </div>
                    </SheetNavLink>
                    <div onClick={logout} className={logoutItem + " " + logoutHover}>
                        <LogOut className="text-inherit" />
                        <p>{t("header.logout.confirm")}</p>
                    </div>
                </>
            ) : (
                <>
                    <SheetNavLink to="/patient/dashboard">
                        <div className={mobileIconContainer}>
                            <ChartPie className={mobileIcon}/>
                            {t("header.patient.dashboard")}
                        </div>
                    </SheetNavLink>
                    <div onClick={logout} className={logoutItem + " " + logoutHover}>
                        <LogOut className="text-inherit" />
                        <p>{t("header.logout.confirm")}</p>
                    </div>
                </>
            )}
        </div>
    );
}

export default Header;